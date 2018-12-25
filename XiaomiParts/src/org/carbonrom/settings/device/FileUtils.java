package org.carbonrom.settings.device;

import android.os.SystemProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class FileUtils {

    static boolean fileWritable(String filename) {
        return fileExists(filename) && new File(filename).canWrite();
    }

    private static boolean fileExists(String filename) {
        if (filename == null) {
            return false;
        }
        return new File(filename).exists();
    }

    static void setValue(String path, Boolean value) {
        if (path == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write((value ? "1" : "0").getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void setValue(String path, int value) {
        if (path == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write(Integer.toString(value).getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void setValue(String path, double value) {
        if (path == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write(Long.toString(Math.round(value)).getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void setProp(String prop, boolean value) {
        if (value) {
            SystemProperties.set(prop, "1");
        } else {
            SystemProperties.set(prop, "0");
        }
    }

    static void setProp(String prop, String value) {
        SystemProperties.set(prop, value);
    }

    static boolean getProp(String prop, boolean defaultValue) {
        return SystemProperties.getBoolean(prop, defaultValue);
    }
}


