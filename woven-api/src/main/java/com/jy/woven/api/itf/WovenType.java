package com.jy.woven.api.itf;

import com.jy.woven.api.ActionKind;
import com.jy.woven.api.exception.NoSuchActionException;
import com.jy.woven.api.exception.NoSuchPointcutException;

import java.lang.reflect.Method;

/**
 * @description 自定义woven类型
 * @date: 2020/8/18 10:26
 * @author: jy
 */
public interface WovenType<T> {

    String getName();


    Package getPackage();


    int getModifiers();


    Class<T> getJavaClass();


    Method[] getDeclaredMethods();


    Pointcut getDeclaredPointcut(String name) throws NoSuchPointcutException;


    Pointcut[] getDeclaredPointcuts();


    Action[] getDeclaredAction(ActionKind... ofTypes);


    Action getDeclaredAction(String name) throws NoSuchActionException;
}
