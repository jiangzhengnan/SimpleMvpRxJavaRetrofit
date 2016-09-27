package com.jzn.simplemvprxjavaretrofit.common;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.jzn.simplemvprxjavaretrofit.bean.BookInfo;
import com.jzn.simplemvprxjavaretrofit.view.IBookView;

import butterknife.ButterKnife;

/**
 * Created by jiangzn on 16/9/21.
 */
public class BaseActivity extends AppCompatActivity implements IBookView{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ButterKnife.bind(this);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void loadBookInfo(String bookInfo) {

    }

}
