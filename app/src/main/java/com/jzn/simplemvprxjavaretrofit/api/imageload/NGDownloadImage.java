package com.jzn.simplemvprxjavaretrofit.api.imageload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.jzn.simplemvprxjavaretrofit.utils.TextUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片加载类
 * Created by nangua on 2016/7/8.
 */
public class NGDownloadImage {
    private ExecutorService executorService; //线程池服务
    private NGImageMemoryCache imageMemoryCache;    //内存缓存
    private NGImageFileCache imageFileCache;//文件缓存
    private NGDownloadImageMode downloadImageMode; //图片实例
    private Map<String, View> taskMap;  //任务集合
    private static NGDownloadImage instance; //自身私有化实例
    private int POOL_SIZE = 5;//线程池自定义大小

    /**
     * 设置private以实现单例
     */
    private NGDownloadImage() {
        final int cpuNums = Runtime.getRuntime().availableProcessors();//cpu数
        executorService = Executors.newFixedThreadPool(cpuNums * POOL_SIZE);//初始化线程池
        imageMemoryCache = new NGImageMemoryCache();//初始化内存缓存
        imageFileCache = new NGImageFileCache();    //初始化文件缓存
        downloadImageMode = new NGDownloadImageMode();//初始化自身
        taskMap = new HashMap<>();//初始化任务集合
    }

    //获得唯一实例
    public static synchronized NGDownloadImage getInstance() {
        //判断如果自身实例为空
        if (instance == null) {
            //初始化获得实例
            instance = new NGDownloadImage();
        }
        //返回实例
        return instance;
    }

    /**
     * 添加任务
     * @param url   传入url
     * @param img   传入图片
     */
    public void addTask(String url, ImageView img) {
        //添加任务
        addTask(  url, img, null);
    }

    /**
     * 添加任务
     * @param url   传入url
     * @param img   传入的img
     * @param callback  回调实例
     */
    public void addTask(String url, View img,
                        NGImageCallback callback) {
        //如果传入img为空则返回
        if (img == null) {
            return;
        }
        //如果传入url为空则返回
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //如果传入回调为空则new一个mode类
        if (callback != null) {
            downloadImageMode = new NGDownloadImageMode();
            //设置回调接口
            downloadImageMode.setCallback(callback);
            //设置url
            downloadImageMode.setImgUrl(url);
            //设置img的tag
            img.setTag(downloadImageMode);
        } else {
            //否则设置tag为url
            img.setTag(url);
        }

        //生成Bitmap
        final Bitmap bitmap = imageMemoryCache.getBitmapFromCache(url);
        //如果缓存里有
        if (bitmap != null) {
            //如果有实现的回调接口，则用回调接口加载图片
            if (callback != null) {
                callback.imageLoaded( img, bitmap, downloadImageMode);
            } else {
                //如果没有，则直接设置该图片为bitmap
                if (img instanceof ImageView)
                    ((ImageView) img).setImageBitmap(bitmap);
            }
        } else {
            //如果缓存没有这个图片
            if (taskMap != null) {
                //添加到任务集合里去
                synchronized (taskMap) {
                    final String mapKey = Integer.toString(img.hashCode());
                    //判断是否已经在任务集中
                    if (!taskMap.containsKey(mapKey)) {
                        taskMap.put(mapKey, img);
                    }
                }
            }
        }
    }

    /**
     * 执行任务集中的任务
     */
    public void doTask() {
        //判断如果任务集为空则返回
        if (taskMap == null) {
            return;
        } else {
            //这里使用同步锁防止并发
            synchronized (taskMap) {
                //获得任务集的值
                Collection<View> collection = taskMap.values();
                //遍历
                for (View view : collection) {
                    //如果view不为空则获得其中的tag
                    if (view != null) {
                        //得到tag
                        Object object = view.getTag();
                        String url = "";
                        //通过判断是否属于mode或者imageview来获得相应的tag中存储的url
                        if (object instanceof NGDownloadImageMode) {
                            url = ((NGDownloadImageMode) object).getImgUrl();
                        } else {
                            url = (String) object;
                        }
                        //如果url不为空则下载图片
                        if (!TextUtils.isEmpty(url)) {
                            loadImage(url, view);
                        }
                    }
                }
            }
        }
    }

    /**
     * 下载图片
     * @param url   地址
     * @param img   图片
     */
    private void loadImage(final String url, final View img) {
        loadImage(url, img, null);
    }

    /**
     * 下载图片
     * @param url   地址
     * @param img   图片
     * @param callback  回调实例
     */
    private void loadImage(final String url, final View img,
                           NGImageCallback callback) {
        String temp = String.valueOf(executorService.submit(new TaskWithResult(new TaskHandler(url, img,
                callback), url)));
        //执行任务
      if   (temp.equals(null) ) {
          Log.d("NGDownloadImage","加载图片地址：" + temp + "失败");
      } else {
          Log.d("NGDownloadImage","加载图片地址：" + temp + "成功");
      }
    }

    //自定义的callable
    private class TaskWithResult implements Callable<String> {
        private String url;//地址
        private Handler handler;    //handler

        /**
         * 构造方法
         * @param handler
         * @param url   地址
         */
        public TaskWithResult(Handler handler, String url) {
            //得到实例
            this.url = url;
            this.handler = handler;
        }

        /**
         * 执行方法
         * @return
         * @throws Exception
         */
        @Override
        public String call() throws Exception {
            //初始化一个message，传入bitmap
            final Message message = handler.obtainMessage(0, getBitmap(url));
            //发送消息
            handler.sendMessage(message);
            return url;
        }
    }

    /**
     * 自定义的taskhandler
     */
    private class TaskHandler extends Handler {
        private String url;//图片地址
        private View img;//图片
        private NGImageCallback callback;//回调接口

        /**
         * 构造方法
         * @param url   url地址
         * @param img   img
         * @param callback  回调接口
         */
        public TaskHandler(String url, View img, NGImageCallback callback) {
            //获得实例
            this.url = url;
            this.img = img;
            this.callback = callback;
        }

        @Override
        public void handleMessage(Message msg) {
            //得到tag
            final Object object = img.getTag();
            //如果是mode类
            if (object instanceof NGDownloadImageMode) {
                final NGDownloadImageMode imageMode = (NGDownloadImageMode) object;
                //通过回调实例下载图片
                imageMode.getCallback().imageLoaded(  img,
                        (Bitmap) msg.obj, imageMode);
                if (taskMap != null) {
                    //下载以后从任务集合中移除
                    taskMap.remove(Integer.toString(img.hashCode()));
                }
                //如果是url字符串
            } else if (object instanceof String) {
                if (callback != null) {
                    //调用接口下载图片
                    callback.imageLoaded( img, (Bitmap) msg.obj, url);
                } else {
                    //如果url不为空
                    if (object.equals(url) && msg.obj != null) {
                        final Bitmap bitmap = (Bitmap) msg.obj;
                        //得带bitmap
                        if (bitmap != null) {
                            if (img instanceof ImageView) {
                                //s设置图片资源
                                ((ImageView) img).setImageBitmap(bitmap);
                            }
                        }
                    }
                }
                //移除任务中相应任务
                if (taskMap != null) {
                    taskMap.remove(Integer.toString(img.hashCode()));
                }
            }
        }
    }

    /**
     * @param url
     * @return Bitmap
     */
    public Bitmap getBitmap(String url) {
        //从缓存中搞得到bitmap
        Bitmap bitmap = imageMemoryCache.getBitmapFromCache(url);
        if (bitmap == null) {
            //缓存没有从文件中得到
            bitmap = imageFileCache.getImage(url);
            if (bitmap == null) {
                //都没有从网络下载
                bitmap = getBitmapFromUrl(url);
                if (bitmap != null) {
                    //存到内存和文件缓存
                    imageMemoryCache.addBitmapToCache(url, bitmap);
                    imageFileCache.saveBmpToSd(url,bitmap);
                }
            } else {
                //如果有责添加到内存中去
                imageMemoryCache.addBitmapToCache(url, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 使用httprulconnection通过发送网络请求path获得bitmap
     * @param path
     * @return
     */
    public static Bitmap getBitmapFromUrl(String path) {
        try {
            //获得url
            URL url = new URL(path);
            //打开httprulconnection获得实例
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时时间
            conn.setConnectTimeout(5000);
            //设置Get
            conn.setRequestMethod("GET");
            //连接成功
            if (conn.getResponseCode() == 200) {
                //获得输入流
                InputStream inputStream = conn.getInputStream();
                //得到bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //返回
                return bitmap;
            }
            //错误信息处理
        } catch (Exception e) {
            //打印错误信息
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 回调接口
     */
    public interface NGImageCallback {
        //加载图片方法
          void imageLoaded(View img, Bitmap imageBitmap,
                           NGDownloadImageMode callBackTag);
        //加载图片方法
          void imageLoaded(View img, Bitmap imageBitmap,
                           String imageUrl);
    }
}
