package com.jy.woven.simple;


import android.util.Log;

import com.jy.woven.api.impl.WovenImpl;
import com.jy.woven.api.itf.Action;
import com.jy.woven.api.itf.Pointcut;
import com.jy.woven.api.itf.WovenType;

public class Test {

    private WovenType<LogWoven> wovenType = new WovenImpl<>(LogWoven.class);

    public void test1() {
        Pointcut[] pointcuts = wovenType.getDeclaredPointcuts();
        for (Pointcut pointcut :
                pointcuts) {
            Log.i("YWoven---pointcut", pointcut.getName() + "==" + pointcut.getExpression());
        }
        Action[] actions = wovenType.getDeclaredAction();
        for (Action action : actions
        ) {
            Log.i("YWoven---action", action.getName() + "==" + action.getPointcut());

        }
    }
}
