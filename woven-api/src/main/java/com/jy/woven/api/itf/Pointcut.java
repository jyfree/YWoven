package com.jy.woven.api.itf;

import java.lang.reflect.Method;

/**
 * @description 切入点接口
 * @date: 2020/8/17 10:13
 * @author: jy
 */
public interface Pointcut {
    String getName();

    Method getMethod();

    String getExpression();

    int getModifiers();

    Class<?>[] getParameterTypes();
}
