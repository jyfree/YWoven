package com.jy.woven.simple;


import android.util.Log;

import com.jy.woven.api.impl.WovenImpl;
import com.jy.woven.api.itf.Advice;
import com.jy.woven.api.itf.Pointcut;
import com.jy.woven.api.itf.WovenType;

public class Test {

    private WovenType<LogWoven> wovenType = new WovenImpl<>(LogWoven.class);

    public void test1() {
        Pointcut[] pointcuts = wovenType.getDeclaredPointcuts();
        for (Pointcut pointcut :
                pointcuts) {
            Log.i("test---", pointcut.getName() + "==" + pointcut.getExpression());
        }
        Advice[] advices=wovenType.getDeclaredAdvice();
        for (Advice advice:advices
             ) {
            Log.i("test2---", advice.getName() + "==" + advice.getPointcut());

        }
    }
}
