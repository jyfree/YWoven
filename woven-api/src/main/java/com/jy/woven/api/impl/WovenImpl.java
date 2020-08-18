package com.jy.woven.api.impl;

import com.jy.woven.annotation.After;
import com.jy.woven.annotation.Before;
import com.jy.woven.api.ActionKind;
import com.jy.woven.api.exception.NoSuchActionException;
import com.jy.woven.api.exception.NoSuchPointcutException;
import com.jy.woven.api.itf.Action;
import com.jy.woven.api.itf.Pointcut;
import com.jy.woven.api.itf.WovenType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * @description 自定义woven类型实现类
 * @date: 2020/8/18 10:27
 * @author: jy
 */
public class WovenImpl<T> implements WovenType<T> {

    private Class<T> clazz;
    private Pointcut[] declaredPointcuts = null;
    private Action[] declaredAction = null;

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
    public Action[] getDeclaredAction(ActionKind... ofTypes) {
        Set<ActionKind> types;
        if (ofTypes.length == 0) {
            types = EnumSet.allOf(ActionKind.class);
        } else {
            types = EnumSet.noneOf(ActionKind.class);
            types.addAll(Arrays.asList(ofTypes));
        }
        return getDeclaredAction(types);
    }


    private Action[] getDeclaredAction(Set ofActionTypes) {
        if (declaredAction == null) initDeclaredAction();
        List<Action> actionList = new ArrayList<Action>();
        for (Action a : declaredAction) {
            if (ofActionTypes.contains(a.getKind())) actionList.add(a);
        }
        Action[] ret = new Action[actionList.size()];
        actionList.toArray(ret);
        return ret;
    }

    @Override
    public Action getDeclaredAction(String name) throws NoSuchActionException {
        if (name.equals(""))
            throw new IllegalArgumentException("use getAction(ActionType...) instead for un-named action");
        if (declaredAction == null) initDeclaredAction();
        for (Action a : declaredAction) {
            if (a.getName().equals(name)) return a;
        }
        throw new NoSuchActionException(name);
    }


    private void initDeclaredAction() {
        Method[] methods = clazz.getDeclaredMethods();
        List<Action> actionList = new ArrayList<Action>();
        for (Method method : methods) {
            Action action = asAction(method);
            if (action != null) actionList.add(action);
        }
        declaredAction = new Action[actionList.size()];
        actionList.toArray(declaredAction);
    }

    private Action asAction(Method method) {
        if (method.getAnnotations().length == 0) return null;
        Before beforeAnn = method.getAnnotation(Before.class);
        if (beforeAnn != null) return new ActionImpl(method, beforeAnn.value(), ActionKind.BEFORE);
        After afterAnn = method.getAnnotation(After.class);
        if (afterAnn != null) return new ActionImpl(method, afterAnn.value(), ActionKind.AFTER);
        return null;
    }
}
