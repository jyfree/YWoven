package com.jy.woven.api.impl;

import com.jy.woven.annotation.After;
import com.jy.woven.annotation.Before;
import com.jy.woven.api.AdviceKind;
import com.jy.woven.api.exception.NoSuchAdviceException;
import com.jy.woven.api.exception.NoSuchPointcutException;
import com.jy.woven.api.impl.AdviceImpl;
import com.jy.woven.api.impl.PointcutImpl;
import com.jy.woven.api.itf.Advice;
import com.jy.woven.api.itf.Pointcut;
import com.jy.woven.api.itf.WovenType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class WovenImpl<T> implements WovenType<T> {

    private Class<T> clazz;
    private Pointcut[] declaredPointcuts = null;
    private Advice[] declaredAdvice = null;

    public WovenImpl(Class<T> fromClass) {
        this.clazz = fromClass;
    }


    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public Package getPackage() {
        return clazz.getPackage();
    }

    @Override
    public int getModifiers() {
        return clazz.getModifiers();
    }

    @Override
    public Class<T> getJavaClass() {
        return clazz;
    }


    @Override
    public Method[] getDeclaredMethods() {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> filteredMethods = new ArrayList<>();
        for (Method method : methods) {
            if (isReallyAMethod(method)) filteredMethods.add(method);
        }
        Method[] ret = new Method[filteredMethods.size()];
        filteredMethods.toArray(ret);
        return ret;
    }

    private boolean isReallyAMethod(Method method) {
        if (method.getAnnotations().length == 0) return true;
        if (method.isAnnotationPresent(com.jy.woven.annotation.Pointcut.class)) return false;
        if (method.isAnnotationPresent(Before.class)) return false;
        if (method.isAnnotationPresent(After.class)) return false;
        return true;
    }

    @Override
    public Pointcut getDeclaredPointcut(String name) throws NoSuchPointcutException {
        Pointcut[] pcs = getDeclaredPointcuts();
        for (Pointcut pc : pcs)
            if (pc.getName().equals(name)) return pc;
        throw new NoSuchPointcutException(name);
    }

    @Override
    public Pointcut[] getDeclaredPointcuts() {
        if (declaredPointcuts != null) return declaredPointcuts;
        List<Pointcut> pointcuts = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Pointcut pc = asPointcut(method);
            if (pc != null) pointcuts.add(pc);
        }
        Pointcut[] ret = new Pointcut[pointcuts.size()];
        pointcuts.toArray(ret);
        declaredPointcuts = ret;
        return ret;
    }


    private Pointcut asPointcut(Method method) {
        com.jy.woven.annotation.Pointcut pointcutAnn = method.getAnnotation(com.jy.woven.annotation.Pointcut.class);
        if (pointcutAnn != null) {
            String targetValue = pointcutAnn.value();
            return new PointcutImpl(method.getName(), targetValue, method);
        }
        return null;
    }


    @Override
    public Advice[] getDeclaredAdvice(AdviceKind... ofTypes) {
        Set<AdviceKind> types;
        if (ofTypes.length == 0) {
            types = EnumSet.allOf(AdviceKind.class);
        } else {
            types = EnumSet.noneOf(AdviceKind.class);
            types.addAll(Arrays.asList(ofTypes));
        }
        return getDeclaredAdvice(types);
    }


    private Advice[] getDeclaredAdvice(Set ofAdviceTypes) {
        if (declaredAdvice == null) initDeclaredAdvice();
        List<Advice> adviceList = new ArrayList<Advice>();
        for (Advice a : declaredAdvice) {
            if (ofAdviceTypes.contains(a.getKind())) adviceList.add(a);
        }
        Advice[] ret = new Advice[adviceList.size()];
        adviceList.toArray(ret);
        return ret;
    }

    @Override
    public Advice getDeclaredAdvice(String name) throws NoSuchAdviceException {
        if (name.equals(""))
            throw new IllegalArgumentException("use getAdvice(AdviceType...) instead for un-named advice");
        if (declaredAdvice == null) initDeclaredAdvice();
        for (Advice a : declaredAdvice) {
            if (a.getName().equals(name)) return a;
        }
        throw new NoSuchAdviceException(name);
    }


    private void initDeclaredAdvice() {
        Method[] methods = clazz.getDeclaredMethods();
        List<Advice> adviceList = new ArrayList<Advice>();
        for (Method method : methods) {
            Advice advice = asAdvice(method);
            if (advice != null) adviceList.add(advice);
        }
        declaredAdvice = new Advice[adviceList.size()];
        adviceList.toArray(declaredAdvice);
    }

    private Advice asAdvice(Method method) {
        if (method.getAnnotations().length == 0) return null;
        Before beforeAnn = method.getAnnotation(Before.class);
        if (beforeAnn != null) return new AdviceImpl(method, beforeAnn.value(), AdviceKind.BEFORE);
        After afterAnn = method.getAnnotation(After.class);
        if (afterAnn != null) return new AdviceImpl(method, afterAnn.value(), AdviceKind.AFTER);
        return null;
    }
}
