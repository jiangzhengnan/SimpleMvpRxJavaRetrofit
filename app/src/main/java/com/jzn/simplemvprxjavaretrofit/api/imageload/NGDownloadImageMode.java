package com.jzn.simplemvprxjavaretrofit.api.imageload;

/**
 * Created by nangua on 2016/7/8.
 */
public class NGDownloadImageMode {


    //图片地址
    private String imgUrl;
    //回调实例
    private NGDownloadImage.NGImageCallback callback;



    //得到url
    public String getImgUrl() {
        return imgUrl;
    }
    //设置url
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    //得到回调接口
    public NGDownloadImage.NGImageCallback getCallback() {
        return callback;
    }
    //设置回调接口
    public void setCallback(NGDownloadImage.NGImageCallback callback) {
        this.callback = callback;
    }

}
