package com.dlh.opensourcelib.utils;

import java.io.File;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/4/18
 */
public class FileUtils {
    public static boolean fileIsExists(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }
}
