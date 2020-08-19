package com.jy.woven.simple;


import com.jy.woven.api.PointcutRoute;
import com.jy.woven.generated.LogWoven_Impl;

public class Test {

    private LogWoven_Impl logWoven = new LogWoven_Impl();

    public void test1() {
//        List<Pointcut> pointcuts = logWoven.getDeclaredPointcuts();
//        for (Pointcut pointcut :
//                pointcuts) {
//            Log.i("YWoven---pointcut", pointcut.getName() + "==" + pointcut.getExpression() + "==" + pointcut.getMethod());
//        }
//        List<Action> actions = logWoven.getDeclaredAction();
//        for (Action action : actions
//        ) {
//            Log.i("YWoven---action", action.getName() + "==" + action.getPointcut() + "==" + action.getMethod());
//            try {
//                action.getMethod().invoke(logWoven.getInstance());
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//
//        }
        PointcutRoute.wovenImplList.add(logWoven);
        PointcutRoute.initWovenInfoToMap();
    }
}
