package com.jy.woven.api.impl;

import com.jy.woven.api.AdviceKind;
import com.jy.woven.api.itf.Advice;

import java.lang.reflect.Method;

public class AdviceImpl implements Advice {

    private final AdviceKind kind;
    private final Method adviceMethod;
    private final String pointcut;

    public AdviceImpl(Method adviceMethod, String pointcut, AdviceKind kind) {
        this.kind = kind;
        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
    }

    @Override
    public AdviceKind getKind() {
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
