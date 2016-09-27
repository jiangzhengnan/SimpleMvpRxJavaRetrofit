package com.jzn.simplemvprxjavaretrofit.view;

import com.jzn.simplemvprxjavaretrofit.bean.BookInfo;

/**
 * Created by jiangzn on 16/9/21.
 */
public interface IBookView {
    void showProgress();
    void hideProgress();
    void loadBookInfo(String bookInfo);
}
