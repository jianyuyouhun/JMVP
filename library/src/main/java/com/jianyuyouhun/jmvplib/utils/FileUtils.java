package com.jianyuyouhun.jmvplib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtils {
    public static String fileNameSuffixFilter(String fileName) {
        if (fileName == null)
            return "";
        return fileName.replace(".jpg", "").replace(".amr", "").replace(".txt", "").replace(".gif", "").replace(".JPG", "");
    }

    public static String fileNameSuffixFilterAllType(String fileName) {
        if (fileName == null)
            return "";
        return fileName.replace(".jpg", "").replace(".amr", "").replace(".txt", "").replace(".mp4", "");
    }

    public static String getStringByStream(InputStream is) {
        if (is == null)
            return "";
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuffer sb = new StringBuffer();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void cleanFilePathWithoutFilterFile(String filePath, String fileName) {
        File f = new File(filePath);
        if (f.exists() && f.isDirectory() && f.listFiles().length > 0) {
            File delFile[] = f.listFiles();
            int i = delFile.length;
            for (int j = 0; j < i; j++) {
                if (!delFile[j].getName().equals(fileName)) {
                    if (delFile[j].isDirectory())
                        cleanFilePathWithoutFilterFile(delFile[j].getAbsolutePath(), fileName);
                    delFile[j].delete();
                }
            }
        }
    }

    public static void cleanFilePath(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.isDirectory() && f.listFiles().length > 0) {
            File delFile[] = f.listFiles();
            int i = delFile.length;
            for (int j = 0; j < i; j++) {
                if (delFile[j].isDirectory())
                    cleanFilePath(delFile[j].getAbsolutePath());
                delFile[j].delete();
            }
        }
    }

    public static boolean copyFile(String fromFile, String toFile) {
        try {
            InputStream is = new FileInputStream(fromFile);
            File file = new File(toFile);
            file.getParentFile().mkdirs();
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[8192];
            int c = -1;
            while ((c = is.read(buffer)) > -1) {
                os.write(buffer, 0, c);
            }
            is.close();
            os.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
