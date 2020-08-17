package com.jy.woven.plugin;

import com.android.SdkConstants;

import java.io.File;

public class StringUtils {

    /**
     * jar文件名转格式
     * 如：androidx/appcompat/app/AppCompatActivity.class 转为 androidx.appcompat.app.AppCompatActivity
     *
     * @param name
     * @return
     */
    public static String scanJarTrimName(String name) {
        return trimName(name, 0).replace('/', '.');
    }

    /**
     * dir文件名转格式
     * 如：…………com\jy\woven\simple\MainActivity.class 转为com.jy.woven.simple.MainActivity
     *
     * @param dir
     * @param targetFile
     * @return
     */
    public static String scanDirTrimName(File dir, File targetFile) {
        return trimName(targetFile.getAbsolutePath(), dir.getAbsolutePath().length() + 1)
                .replace(File.separatorChar, '.');
    }

    /**
     * 修剪文件格式
     *
     * @param s
     * @param start
     * @return
     */
    private static String trimName(String s, int start) {
        if (s == null || s.length() < SdkConstants.DOT_CLASS.length()) {
            return s;
        }
        return s.substring(start, s.length() - SdkConstants.DOT_CLASS.length());
    }

}
