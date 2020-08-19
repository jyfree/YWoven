package com.jy.woven.api.impl;

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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * woven实现基类
 *
 * @param <T>
 */
public abstract class BaseWovenImpl<T> implements WovenType<T> {

    public Class<T> clazz;
    public List<Pointcut> declaredPointcuts = null;
    public List<Action> declaredActions = null;
    public HashMap<String, Method> declaredMethodMap = null;
    private Object instance;

    public BaseWovenImpl(Class<T> fromClass) {
        this.clazz = fromClass;
        init();
        getDeclaredMethods();
    }

    private void init() {
        try {
            instance = this.clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getInstance() {
        return instance;
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
    public HashMap<String, Method> getDeclaredMethods() {
        if (declaredMethodMap == null) {
            declaredMethodMap = initMethod();
        }
        if (declaredMethodMap == null) {
            Method[] methods = clazz.getDeclaredMethods();
            declaredMethodMap = new HashMap<>();
            for (Method mt : methods) {
                declaredMethodMap.put(mt.getName(), mt);
            }
        }
        return declaredMethodMap;
    }


    @Override
    public List<Pointcut> getDeclaredPointcuts() {
        if (declaredPointcuts == null) {
            declaredPointcuts = initDeclaredPointcuts();
        }
        return declaredPointcuts;
    }

    @Override
    public Pointcut getDeclaredPointcut(String name) throws NoSuchPointcutException {
        List<Pointcut> pcs = getDeclaredPointcuts();
        for (Pointcut pc : pcs)
            if (pc.getName().equals(name)) return pc;
        throw new NoSuchPointcutException(name);
    }


    @Override
    public List<Action> getDeclaredAction(ActionKind... ofTypes) {
        Set<ActionKind> types;
        if (ofTypes.length == 0) {
            types = EnumSet.allOf(ActionKind.class);
        } else {
            types = EnumSet.noneOf(ActionKind.class);
            types.addAll(Arrays.asList(ofTypes));
        }
        return getDeclaredAction(types);
    }


    private List<Action> getDeclaredAction(Set ofActionTypes) {
        if (declaredActions == null) {
            declaredActions = initDeclaredAction();
        }
        List<Action> actionList = new ArrayList<Action>();
        for (Action a : declaredActions) {
            if (ofActionTypes.contains(a.getKind())) actionList.add(a);
        }
        return actionList;
    }

    @Override
    public Action getDeclaredAction(String name) throws NoSuchActionException {
        if (name.equals(""))
            throw new IllegalArgumentException("use getAction(ActionType...) instead for un-named action");
        if (declaredActions == null) {
            declaredActions = initDeclaredAction();
        }
        for (Action a : declaredActions) {
            if (a.getName().equals(name)) return a;
        }
        throw new NoSuchActionException(name);
    }

    public abstract HashMap<String, Method> initMethod();

    public abstract List<Pointcut> initDeclaredPointcuts();

    public abstract List<Action> initDeclaredAction();
}
