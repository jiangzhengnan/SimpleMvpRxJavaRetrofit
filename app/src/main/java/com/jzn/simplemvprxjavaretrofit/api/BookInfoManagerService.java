package com.jzn.simplemvprxjavaretrofit.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by jiangzn on 16/9/27.
 */
public interface BookInfoManagerService {
    //https://api.douban.com/v2/book/isbn/:9787121267734
    @GET("v2/book/{isbn}")
    Observable<String> getBookInfo(@Path("isbn") String isbn);

}
