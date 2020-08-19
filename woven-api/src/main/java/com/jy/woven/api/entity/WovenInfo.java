package com.jy.woven.api.entity;

import com.jy.woven.api.ActionKind;

import java.lang.reflect.Method;

/**
 * @description 编织信息
 * @date: 2020/8/19 13:44
 * @author: jy
 */
public class WovenInfo {

    private String targetMethodName;//目标方法（需要切入代码的方法名）
    private Method executeMethod;//执行方法（切入的代码）
    private ActionKind actionKind;//执行方式（方法前切，后切）
    private Object instance;//BaseWovenImpl对象

    public WovenInfo(String targetMethodName, Method executeMethod, ActionKind actionKind, Object instance) {
        this.targetMethodName = targetMethodName;
        this.executeMethod = executeMethod;
        this.actionKind = actionKind;
        this.instance = instance;
    }

    public String getTargetMethodName() {
        return targetMethodName;
    }

    public Method getExecuteMethod() {
        return executeMethod;
    }

    public ActionKind getActionKind() {
        return actionKind;
    }

    public Object getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "WovenInfo{" +
                "targetMethodName='" + targetMethodName + '\'' +
                ", executeMethod=" + executeMethod +
                ", actionKind=" + actionKind +
                ", instance=" + instance +
                '}';
    }
}
