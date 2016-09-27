package com.jzn.simplemvprxjavaretrofit.model;

import com.jzn.simplemvprxjavaretrofit.api.BookInfoManager;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jiangzn on 16/9/27.
 */
public class BookModel implements IBookModel {
    private BookOnListener mBookOnListener;

    public BookModel(BookOnListener mBookOnListener) {
        this.mBookOnListener = mBookOnListener;
    }

    @Override
    public Subscription getBookData(String isbn) {
        Observable<String> request = BookInfoManager.getBookInfo(isbn);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                               @Override
                               public void call(String s) {
                                mBookOnListener.onSuccess(s);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mBookOnListener.onFailure(throwable);
                            }
                        });
        return sub;
    }

    /**
     * 请求结果回调接口
     */
    public interface BookOnListener {
        void onSuccess(String s);

        void onFailure(Throwable e);
    }
}
