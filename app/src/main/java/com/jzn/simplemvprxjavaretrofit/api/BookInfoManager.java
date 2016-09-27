package com.jzn.simplemvprxjavaretrofit.api;

import com.jzn.simplemvprxjavaretrofit.Config;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

/**
 * 使用Retrofit获取图书信息
 * Created by jiangzn on 16/9/27.
 */
public class BookInfoManager {


    private static final Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(Config.QUERY_PATH)
            //增加返回值为String的支持
            .addConverterFactory(ScalarsConverterFactory.create())
            //增加返回值为Gson的支持(以实体类返回)
            .addConverterFactory(GsonConverterFactory.create())
            //增加返回值为Oservable<T>的支持
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private static final BookInfoManagerService mManager = sRetrofit.create(BookInfoManagerService.class);

    public static Observable<String> getBookInfo(String isbn) {
        return mManager.getBookInfo(isbn);
    }

}
