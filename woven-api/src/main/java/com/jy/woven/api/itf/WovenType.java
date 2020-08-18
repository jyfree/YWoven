package com.jy.woven.api.itf;

import com.jy.woven.api.ActionKind;
import com.jy.woven.api.exception.NoSuchActionException;
import com.jy.woven.api.exception.NoSuchPointcutException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

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


    HashMap<String, Method> getDeclaredMethods();


    Pointcut getDeclaredPointcut(String name) throws NoSuchPointcutException;


    List<Pointcut> getDeclaredPointcuts();


    List<Action> getDeclaredAction(ActionKind... ofTypes);


    Action getDeclaredAction(String name) throws NoSuchActionException;
}
