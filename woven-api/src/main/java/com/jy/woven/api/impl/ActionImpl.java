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

    private final ActionKind kind;
    private final Method adviceMethod;
    private final String pointcut;

    public ActionImpl(Method adviceMethod, String pointcut, ActionKind kind) {
        this.kind = kind;
        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
    }

    @Override
    public ActionKind getKind() {
        return kind;
    }

    @Override
    public String getName() {
        return adviceMethod.getName();
    }

    @Override
    public String getPointcut() {
        return pointcut;
    }


    @Override
    public int getModifiers() {
        return adviceMethod.getModifiers();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return adviceMethod.getParameterTypes();
    }
}
