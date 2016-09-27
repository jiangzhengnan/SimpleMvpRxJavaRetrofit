package com.jzn.simplemvprxjavaretrofit.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by nangua on 2016/7/8.
 */
public class DevicesUtils {
    /**
     * 得到sd卡剩余空间
     * @return
     */
    public static int freeSpaceOnSd() {
        long val=0;
        //得到data文件目录对象
        File path = Environment.getDataDirectory();
        //获得stat
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();//获得分区长度
        long availableBlocks = stat.getAvailableBlocksLong();//获得分区数量
        val=availableBlocks * blockSize;//得到总数
        return (int) val;
    }
}
