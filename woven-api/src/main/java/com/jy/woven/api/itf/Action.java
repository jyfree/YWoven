package com.jy.woven.api.itf;

import com.jy.woven.api.ActionKind;

import java.lang.reflect.Method;

/**
 * @description 切入行为接口
 * @date: 2020/8/18 10:25
 * @author: jy
 */
public interface Action {
    ActionKind getKind();

    String getName();

    Method getMethod();

    String getPointcut();

    int getModifiers();

    Class<?>[] getParameterTypes();
}
