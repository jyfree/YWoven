package com.jy.woven.plugin;


import com.jy.woven.api.PointcutRoute;

import java.io.File;

public class MatchUtils {

    /**
     * 查找需要修改的文件(jar)
     *
     * @param name 对应的文件
     * @return
     */
    public static boolean matchJarFile(String name) {
        if (filterClass(name)) {
            String trimName = StringUtils.scanJarTrimName(name);
            if (matchTrimName(trimName)) {
                Logger.info("-------------------------matching from jarInputs-------------------------");
                Logger.info("Transform: matching is ---->%s", trimName);
                return true;
            }
        }
        return false;
    }

    /**
     * 查找需要修改的文件(dir)
     *
     * @param dir  文件路径
     * @param file 文件
     * @return
     */
    public static boolean matchDirFile(File dir, File file) {
        String name = file.getName();
        if (filterClass(name)) {
            String trimName = StringUtils.scanDirTrimName(dir, file);
            if (matchTrimName(trimName)) {
                Logger.info("matchDirFile %s--diao ni ", PointcutRoute.test);
                Logger.info("-------------------------matching from directoryInputs-------------------------");
                Logger.info("Transform: matching is ---->%s", trimName);
                return true;
            }
        }
        return false;
    }

    /**
     * 查找需要修改的类
     *
     * @param name
     * @return
     */
    public static boolean matchClass(String name) {
        String tmpName = name.replace("/", ".");
        return matchTrimName(tmpName);
    }


    /**
     * 过滤R.class、R$、BuildConfig.class等文件
     *
     * @param name
     * @return
     */
    private static boolean filterClass(String name) {
        return name.endsWith(".class") && !name.startsWith("R\\$")
                && !"R.class".equals(name) && !"BuildConfig.class".equals(name);
    }


    /**
     * @param name
     * @return
     */
    private static boolean matchTrimName(String name) {
        return "androidx.appcompat.app.AppCompatActivity".equals(name) || "com.jy.woven.simple.TestJavaActivity".equals(name) ||
                "com.jy.woven.simple.MainActivity".equals(name) || "com.jy.woven.simple.LogWoven".equals(name);
    }

}
