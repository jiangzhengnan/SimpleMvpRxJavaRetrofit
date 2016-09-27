package com.jzn.simplemvprxjavaretrofit.utils;

/**
 * 文本工具类
 * Created by nangua on 2016/7/8.
 */
public class TextUtils {

    //判断是否为空
    public static boolean isEmpty(String txt) {
        //判断引用对象是否为空，内容是否为空，长度是否为0
        if ((txt == null)||(txt.equals(null))||(txt.length()==0))
            return true;
        else
            return false;
    }
}
