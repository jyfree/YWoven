package com.jy.woven.api.impl;

import com.jy.woven.api.itf.Pointcut;

import java.lang.reflect.Method;

/**
 * @description 切入点实现类
 * @date: 2020/8/17 10:13
 * @author: jy
 */
public class PointcutImpl implements Pointcut {

    private final Method method;
    private final String expression;

    public PointcutImpl(Method method, String expression) {
        this.method = method;
        this.expression = expression;
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
    public String getExpression() {
        return expression;
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
