package com.jzn.simplemvprxjavaretrofit.presenter;

import android.util.Log;

import com.jzn.simplemvprxjavaretrofit.bean.BookInfo;
import com.jzn.simplemvprxjavaretrofit.model.BookModel;
import com.jzn.simplemvprxjavaretrofit.model.IBookModel;
import com.jzn.simplemvprxjavaretrofit.utils.LogUtils;
import com.jzn.simplemvprxjavaretrofit.view.IBookView;

/**
 * Created by jiangzn on 16/9/27.
 */
public class BookPresenter extends BasePresenter implements IBookPresenter,BookModel.BookOnListener {
    private IBookModel mBookModel;
    private IBookView mBookView;

    public BookPresenter(IBookView mBookView) {
        this.mBookView = mBookView;
        this.mBookModel = new BookModel(this);
    }

    @Override
    public void getBookInfo(String isbn) {
        mBookView.showProgress();
        addSubscription(mBookModel.getBookData(isbn));
    }


    @Override
    public void onSuccess(String s) {
        mBookView.loadBookInfo(s);
        mBookView.hideProgress();
        LogUtils.d("onSuccess() :" + s.toString());
    }

    @Override
    public void onFailure(Throwable e) {
        mBookView.hideProgress();
        LogUtils.d("onFailure() :" + e.getMessage());
    }
}
