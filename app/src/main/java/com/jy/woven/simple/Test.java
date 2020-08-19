package com.jy.woven.simple;


import com.jy.woven.api.PointcutRoute;
import com.jy.woven.generated.LogWoven_Impl;

public class Test {

    private LogWoven_Impl logWoven = new LogWoven_Impl();

    public void test1() {
        PointcutRoute.wovenImplList.add(logWoven);
        PointcutRoute.initWovenInfoToMap();
    }
}
