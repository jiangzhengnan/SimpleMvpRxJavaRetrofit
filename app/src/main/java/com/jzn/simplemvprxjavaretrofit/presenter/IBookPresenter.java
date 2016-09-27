package com.jzn.simplemvprxjavaretrofit.presenter;

/**
 * Created by jiangzn on 16/9/27.
 */
public interface IBookPresenter {
    /**
     * 获取天气信息
     * @param isbn
     */
    void getBookInfo(String isbn);
}
