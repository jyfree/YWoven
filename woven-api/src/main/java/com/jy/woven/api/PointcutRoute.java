package com.jy.woven.api;

import com.jy.woven.api.entity.ActionInfo;
import com.jy.woven.api.entity.WovenInfo;
import com.jy.woven.api.impl.BaseWovenImpl;
import com.jy.woven.api.itf.Action;
import com.jy.woven.api.itf.Pointcut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description 切点路由
 * @date: 2020/8/19 13:44
 * @author: jy
 */
public class PointcutRoute {

    //编织信息
    public static final HashMap<String, List<WovenInfo>> wovenInfoMap = new HashMap<>();

    //编织实现类集合
    public static List<BaseWovenImpl<?>> wovenImplList = new ArrayList<>();

    //切面类型信息
    private static HashMap<String, ActionInfo> actionInfoMap = new HashMap<>();

    public static final String test = "fuck you";

    public static void initWovenInfoToMap() {

        if (wovenInfoMap.size() > 0) {
            return;
        }
        String actionKeyFormat = "%s_%s";
        //获取action信息
        for (BaseWovenImpl<?> woven : wovenImplList) {
            //获取action信息
            List<Action> actionList = woven.getDeclaredAction();
            for (Action action : actionList) {
                String value = action.getPointcut();
                ActionInfo actionInfo = new ActionInfo(action.getMethod(), action.getKind());
                if (value.contains("@")) {
                    String key = String.format(actionKeyFormat, woven.getName(), action.getName());
                    actionInfoMap.put(key, actionInfo);

//                    Log.i("YWoven--action--", key + "==" + action.getMethod());
                } else {
                    String name = value.substring(0, value.indexOf("("));
                    String key = String.format(actionKeyFormat, woven.getName(), name);
                    actionInfoMap.put(key, actionInfo);

//                    Log.i("YWoven--action--", key + "==" + action.getMethod());
                }
            }
        }

        //获取编织信息
        for (BaseWovenImpl<?> woven : wovenImplList) {

            //action类型的编织
            List<Action> actionList = woven.getDeclaredAction();
            for (Action action : actionList) {

                String key = String.format(actionKeyFormat, woven.getName(), action.getName());
                addWovenInfo(action.getPointcut(), key, woven.getInstance());
            }
            //pointcut类型的编织
            List<Pointcut> pointcutList = woven.getDeclaredPointcuts();
            for (Pointcut pointcut : pointcutList) {
                String key = String.format(actionKeyFormat, woven.getName(), pointcut.getName());
                addWovenInfo(pointcut.getExpression(), key, woven.getInstance());
            }
        }
    }

    private static void addWovenInfo(String pointcut, String actionKey, Object instance) {

        if (pointcut.contains("@") && (pointcut.contains("()") || pointcut.contains("(*)"))) {
            //切入点
            String classPath = pointcut.substring(pointcut.indexOf("@") + 1, pointcut.lastIndexOf("."));
            //目标方法（需要切入代码的方法名）
            String targetMethodName;
            if (pointcut.contains("()")) {
                targetMethodName = pointcut.substring(pointcut.lastIndexOf(".") + 1);
            } else {
                targetMethodName = pointcut.substring(pointcut.lastIndexOf(".") + 1, pointcut.lastIndexOf("("));
            }
            ActionInfo actionInfo = actionInfoMap.get(actionKey);
            if (actionInfo == null) return;
            WovenInfo wovenInfo = new WovenInfo(targetMethodName, actionInfo.getMethod(), actionInfo.getActionKind(), instance);
//            Log.i("YWoven--wovenInfo--", classPath + "==" + wovenInfo.toString());
            addWovenInfoToMap(classPath, wovenInfo);
        }
    }


    private static void addWovenInfoToMap(String classPath, WovenInfo wovenInfo) {
        List<WovenInfo> wovenInfoList = wovenInfoMap.get(classPath);
        if (wovenInfoList == null) {
            wovenInfoList = new ArrayList<>();
            wovenInfoMap.put(classPath, wovenInfoList);
        }
        if (!wovenInfoList.contains(wovenInfo)) {
            wovenInfoList.add(wovenInfo);
        }
    }
}
