package com.jzn.simplemvprxjavaretrofit.model;

import rx.Subscription;

/**
 * Created by jiangzn on 16/9/27.
 */
public interface IBookModel {
    /**
     * 根据isbn获取书本信息
     * @param isbn
     * @return
     */
    Subscription getBookData(String isbn);
}
