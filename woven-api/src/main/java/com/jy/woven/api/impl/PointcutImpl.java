package com.jy.woven.api.impl;

import com.jy.woven.api.itf.Pointcut;

import java.lang.reflect.Method;

/**
 * @description 切入点实现类
 * @date: 2020/8/17 10:13
 * @author: jy
 */
public class PointcutImpl implements Pointcut {
    private final String name;
    private final String expression;
    private final Method baseMethod;

    public PointcutImpl(String name, String expression, Method baseMethod) {
        this.name = name;
        this.expression = expression;
        this.baseMethod = baseMethod;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public int getModifiers() {
        return baseMethod.getModifiers();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return baseMethod.getParameterTypes();
    }
}
