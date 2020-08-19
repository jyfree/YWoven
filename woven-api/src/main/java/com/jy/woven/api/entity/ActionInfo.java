package com.jy.woven.api.entity;

import com.jy.woven.api.ActionKind;

import java.lang.reflect.Method;

/**
 * @description 切点行为信息
 * @date: 2020/8/19 13:43
 * @author: jy
 */
public class ActionInfo {

    private Method method;
    private ActionKind actionKind;

    public ActionInfo(Method method, ActionKind actionKind) {
        this.method = method;
        this.actionKind = actionKind;
    }

    public Method getMethod() {
        return method;
    }

    public ActionKind getActionKind() {
        return actionKind;
    }
}
