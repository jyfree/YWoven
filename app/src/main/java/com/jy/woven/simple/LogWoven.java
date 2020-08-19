package com.jy.woven.simple;

import android.util.Log;

import com.jy.woven.annotation.After;
import com.jy.woven.annotation.Before;
import com.jy.woven.annotation.Pointcut;
import com.jy.woven.annotation.Woven;

@Woven
public class LogWoven {
    @Pointcut("@com.jy.woven.simple.TestJavaActivity.test()")
    public void executionTest() {
    }

    @After("executionTest()")
    public void testAfter() {
        Log.i("YWoven", "test After");
    }

    @Before("@com.jy.woven.simple.TestJavaActivity.test()")
    public void testBefore() {
        Log.i("YWoven", "test Before");
    }

    @Before("@com.jy.woven.simple.TestJavaActivity.test2(*)")
    public void testBefore2() {
        Log.i("YWoven", "test Before 2");
    }
}
