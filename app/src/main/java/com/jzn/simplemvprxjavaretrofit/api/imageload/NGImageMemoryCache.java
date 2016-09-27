package com.jzn.simplemvprxjavaretrofit.api.imageload;

import android.graphics.Bitmap;


import com.jzn.simplemvprxjavaretrofit.utils.TextUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存缓存
 * 使用强弱引用map实现
 * Created by nangua on 2016/7/8.
 */
public class NGImageMemoryCache {
    private static final int MAX_CACHE_CAPACITY = 40; //自定义最大容量
    private HashMap<String, Bitmap> hashMap; //缓存集合
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache =
            new ConcurrentHashMap<>(MAX_CACHE_CAPACITY);       //线程安全的软引用缓存集合

    /**ntgtggggggg
     * 初始化
     * 淘汰最老的键
     */
    protected NGImageMemoryCache() {
        //使用LinkedHashMap保证有序读取
        hashMap = new LinkedHashMap<String, Bitmap>(MAX_CACHE_CAPACITY, 0.75f, true) {
            //移除hashmap中最老的键值
            @Override
            protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
                //如果佔用內存過大
                if (size() > MAX_CACHE_CAPACITY) {
                    mSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                    return true; //返回true则移除最老的键值
                } else {
                    return false;   //返回false就不执行移除操作
                }
            }
        };
    }

    /**
     * 从内存中获取Bitmap
     * @param url
     * @return
     */
    public Bitmap getBitmapFromCache(String url) {
        //判断强引用集合可用
        if (hashMap != null && hashMap.size() > 0) {
            //锁住防止并发
            synchronized (hashMap) {
                try {
                    //得到bitmap
                    final Bitmap bitmap = hashMap.get(url);
                    //判断bitmap不为空
                    if (bitmap != null) {
                        // hashMap.remove(url);
                        hashMap.put(url, bitmap);//存入内存
                        return bitmap;
                    }
                    //抓错
                } catch (OutOfMemoryError e) {
                    //打印输出
                    e.printStackTrace();
                }
            }
        }
        //如果hashmap为空，则从软缓存map里取并放入hashmap
        if (mSoftBitmapCache != null && !TextUtils.isEmpty(url)) {
            //存放进软引用
            final SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(url);
            //如果bitmmaorefrence不为空
            if (bitmapReference != null) {
                try {
                    //得到文件的bitmap
                    final Bitmap bitmap = bitmapReference.get();
                    //如果bitmap不为空
                    if (bitmap != null) {
                        //存到强软引用中
                        hashMap.put(url, bitmap);
                        //软引用中清楚
                        mSoftBitmapCache.remove(url);
                        return bitmap;
                    } else {
                        //清楚
                        mSoftBitmapCache.remove(url);
                    }
                    //抓错
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 添加键值到内存
     * @param url
     * @param bitmap
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        //如果bitmap不为空
        if (bitmap != null) {
            //锁住
            synchronized (hashMap) {
                //放入强引用
                hashMap.put(url,bitmap);
            }
        }
    }

    /**
     * 从内存中删除
     * @param url
     */
    public void deleteBitmapFromCache(String url) {
        //锁住防止并发
        synchronized (hashMap) {
            if (hashMap.containsKey(url)) {
                //从hashmap中移除
                hashMap.remove(url);
            }
        }
        //如果软引用中包含则删除
        if (mSoftBitmapCache.contains(url)) {
            //从软集合移除
            mSoftBitmapCache.remove(url);
        }
    }

    /**
     * 清空缓存
     */
    public void deleteAllBitmapFromCache() {
        //锁住防止并发
        synchronized (hashMap) {
            //清空强引用集合
            hashMap.clear();
        }
        //清空软引用集合
        mSoftBitmapCache.clear();
    }

}
