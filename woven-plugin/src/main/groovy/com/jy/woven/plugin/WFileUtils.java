package com.jy.woven.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WFileUtils {
    public static byte[] toByte(File file) throws IOException {

        InputStream input = new FileInputStream(file);
        byte[] byt = new byte[input.available()];
        input.read(byt);
        return byt;
    }

}
