package com.jzn.simplemvprxjavaretrofit.api.imageload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


import com.jzn.simplemvprxjavaretrofit.utils.DevicesUtils;
import com.jzn.simplemvprxjavaretrofit.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * 文件缓存类
 * Created by nangua on 2016/7/8.
 */
public class NGImageFileCache {
    private int MB = 1024 * 1024;   //MB大小
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 60; //自定义文件最大容量60MB
    private static final String CACHE_SUFFIX = ".cache";    //文件后缀名
    private static final int CACHE_SIZE = 30; //30MB
    private static final float REDUCTION_RATIO = 0.5f;  //移除因子

    /**
     * 构造方法设置protected
     * 防止其他包调用这个方法
     */
    protected NGImageFileCache() {
        //先做清空缓存判断
        removeChache(FileUtils.getImgDirectory());
    }

    /**
     * 从文件缓存中移除
     * @param dirPath   要移除的文件路径
     * @return
     */
    private boolean removeChache(String dirPath) {
        //文件路径对象
        final File dir = new File(dirPath);
        //得到所有文件
        final File[] files = dir.listFiles();
        //如果没有则返回
        if (files == null) {
            return true;
        }

        //如果存储设备不存在
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }

        int dirSize = 0;    //缓存文件总数
        for (int i = 0; i < files.length; i++) {
            //如果包含缓存文件
            if (files[i].getName().contains(CACHE_SUFFIX)) {
                //则总数++
                dirSize += files[i].length();
            }
        }

        //内存空间判断,按从新到旧与指定比例缩减
        if (dirSize > CACHE_SIZE * MB
                || FREE_SD_SPACE_NEEDED_TO_CACHE > DevicesUtils.freeSpaceOnSd()) {
            //设置移除文件数量为文件长度乘以移除因子
            //+1为了防止归0
            final int removeFactor = (int) ((REDUCTION_RATIO * files.length) + 1);
            //把文件按更新时间排序
            Arrays.sort(files,new FileUtils.FileLastModifSort());
            for (int i = 0; i<removeFactor;i++) {
                //得到包含后缀名的文件
                if (files[i].getName().contains(CACHE_SUFFIX)) {
                    //删掉它
                    files[i].delete();
                }
            }
        }

        //如果剩余内存小于缓存空间大小
        if (DevicesUtils.freeSpaceOnSd() <= CACHE_SIZE*MB) {
            //返回false
            return false;
        }
        //返回true
        return  true;
    }

    /**
     * 从文件缓存中得到相应的图片文件
     * @param url   图片url
     * @return
     */
    public Bitmap getImage(final String url) {
        //文件路径
        final String path = FileUtils.getImgDirectory() + "/" +
                FileUtils.convertUrlToFileName(url);
        //图片对象
        final File file = new File(path);
        //如果文件存在
        if (file.exists()) {
            //得到bitmap并返回
            try {
                final Bitmap bmp = BitmapFactory.decodeFile(path);
                //如果bmp为空就删除这个废物文件
                if (bmp == null) {
                    file.delete();
                } else {
                    //更新文件名
                    FileUtils.convertUrlToFileName(url);
                }
                //抓错
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 存储图片
     * @param bitmap
     * @param url
     */
    public void saveBmp(Bitmap bitmap, String url) {
        //判断图片是否为空
        if (bitmap == null) {
            return;
        }
        //判断设备剩余空间大小
        if (FREE_SD_SPACE_NEEDED_TO_CACHE*MB > DevicesUtils.freeSpaceOnSd()) {
            return;
        }
        //存储到sd
        saveBmpToSd(url, bitmap);
    }

    /**
     * 存储图片到SD卡
     * @param url
     * @param bitmap
     */
    public void saveBmpToSd(String url, Bitmap bitmap) {
        try {
            //判断SD卡是否可用
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String fileDirectory = FileUtils.getImgDirectory();//得到目录
                File dir = new File(fileDirectory); //得到目录实例
                //如果文件夹不存在
                if (!dir.exists()) {
                    dir.mkdirs();//创建目录
                }
                //更新文件名
                String fileName = FileUtils.convertUrlToFileName(url);
                //得到文件实例
                File file = new File(fileDirectory + "/" + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                    //创建文件
                    final OutputStream outputStream = new FileOutputStream(file);
                    //设置输出流
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    //是用compress方法读入图片
                    outputStream.flush();
                    //关闭资源
                    outputStream.close();

                }
            }
//抓错
        } catch (Exception e) {

        }
    }

    /**
     * 删除内存中图片
     * @param url
     * @return
     */
    public boolean deleteImage(final String url) {
        //文件路徑
        final String path = FileUtils.getImgDirectory() + "/" + FileUtils.convertUrlToFileName(url);
        //文件類
        final File file = new File(path);
        if (file.exists()) {
            //刪除文件
            return file.delete();
        }
        return false;
    }


}
