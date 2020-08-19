package com.jy.woven.api.impl;

import com.jy.woven.api.ActionKind;
import com.jy.woven.api.itf.Action;

import java.lang.reflect.Method;

/**
 * @description 行为实现类
 * @date: 2020/8/18 10:24
 * @author: jy
 */
public class ActionImpl implements Action {

    private final Method method;
    private final String pointcut;
    private final ActionKind kind;


    public ActionImpl(Method method, String pointcut, ActionKind kind) {
        this.method = method;
        this.pointcut = pointcut;
        this.kind = kind;
    }

    @Override
    public ActionKind getKind() {
        return kind;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public String getPointcut() {
        return pointcut;
    }


    @Override
    public int getModifiers() {
        return method.getModifiers();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }
}
