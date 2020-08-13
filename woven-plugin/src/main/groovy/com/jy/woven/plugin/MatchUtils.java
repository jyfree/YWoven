package com.jy.woven.plugin;

public class MatchUtils {

    /**
     * 查找需要修改的文件
     *
     * @param name  对应的文件
     * @param isJar true：jar文件，false：文件目录下文件
     * @return
     */
    public static boolean matchLifecycleFileName(String name, boolean isJar) {
        if (name.endsWith(".class") && !name.startsWith("R\\$")
                && !"R.class".equals(name) && !"BuildConfig.class".equals(name)
                && ("androidx/appcompat/app/AppCompatActivity.class".equals(name) || "TestJavaActivity.class".equals(name) || "MainActivity.class".equals(name))) {
            Logger.info("-------------------------matching from %s-------------------------", isJar ? "jarInputs" : "directoryInputs");
            Logger.info("Transform: matching is ---->%s", name);
            return true;
        }
        return false;
    }

    /**
     * 查找需要修改的类
     *
     * @param name
     * @return
     */
    public static boolean matchLifecycleClassName(String name) {
        if ("androidx/appcompat/app/AppCompatActivity".equals(name) || "com/jy/woven/simple/TestJavaActivity".equals(name) ||
                "com/jy/woven/simple/MainActivity".equals(name)) {
            return true;
        }
        return false;
    }
}
