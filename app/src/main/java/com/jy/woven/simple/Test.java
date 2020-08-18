package com.jy.woven.simple;


import android.util.Log;

import com.jy.woven.api.itf.Action;
import com.jy.woven.api.itf.Pointcut;
import com.jy.woven.generated.LogWoven_Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Test {

    private LogWoven_Impl logWoven = new LogWoven_Impl();

    public void test1() {
        List<Pointcut> pointcuts = logWoven.getDeclaredPointcuts();
        for (Pointcut pointcut :
                pointcuts) {
            Log.i("YWoven---pointcut", pointcut.getName() + "==" + pointcut.getExpression() + "==" + pointcut.getMethod());
        }
        List<Action> actions = logWoven.getDeclaredAction();
        for (Action action : actions
        ) {
            Log.i("YWoven---action", action.getName() + "==" + action.getPointcut() + "==" + action.getMethod());
            try {
                action.getMethod().invoke(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }
}
