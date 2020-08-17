package com.jy.woven.api.itf;

/**
 * @description 切入点接口
 * @date: 2020/8/17 10:13
 * @author: jy
 */
public interface Pointcut {
    String getName();

    String getExpression();

    int getModifiers();

    Class<?>[] getParameterTypes();
}
