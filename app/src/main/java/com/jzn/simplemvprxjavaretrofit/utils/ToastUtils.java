package com.jzn.simplemvprxjavaretrofit.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jiangzn on 16/9/27.
 */
public class ToastUtils {

    public static void makeText(Context context,String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
