package com.jzn.simplemvprxjavaretrofit.utils;

import android.os.Environment;


import com.jzn.simplemvprxjavaretrofit.Config;

import java.io.File;
import java.util.Comparator;

/**
 * Created by nangua on 2016/7/8.
 */
public class FileUtils {

    /**
     * 根据文件更新时间排序
     */
    public static class FileLastModifSort implements Comparator<File> {
        //比较两个文件
        public int compare(File arg0, File arg1) {
            //如果第一个新返回1
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
                //相等返回0
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 得到图片缓存目录
     * @return
     */
    public static String getImgDirectory() {
        //得到系统sd文件目录对象
        File sdcardDir = Environment.getExternalStorageDirectory();
        //获得路径
        String path = sdcardDir.getParent() + "/" + sdcardDir.getName();
        //设置图片路径
        final String fileDirectory = path + "/"
                + Config.CACHEIR_IMAGE_PATH;
        return fileDirectory;
    }

    /**
     * 从URL中得到文件名
     * @param url
     * @return
     */
    public static String convertUrlToFileName(String url) {
        //设置分隔符
        final String[] strs = url.split("/");
        //设置temp
        String fileName = strs[strs.length - 1];
        //得到文件名
        if (fileName.contains("@")) {
            String[] mystr = url.split("@");
            fileName = mystr[0];
        }
        return fileName;
    }

    /**
     * 更新文件时间
     * @param path
     */
    public void updateFileTime(String path) {
        //获得文件类
        final File file = new File(path);
        //获得系统时间
        final long newModifiedTime = System.currentTimeMillis();
        //设置文件更新时间
        file.setLastModified(newModifiedTime);
    }
}
