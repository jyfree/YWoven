package com.jy.woven;

import com.jy.woven.annotation.common.Const;

import java.lang.reflect.InvocationTargetException;

public class YWoven {

    public static void init() {
        try {
            Class.forName(Const.GEN_PKG + "." + Const.POINTCUT_ROUTE_CONFIG_NAME)
                    .getMethod(Const.INIT_METHOD)
                    .invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
