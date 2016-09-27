package com.jzn.simplemvprxjavaretrofit;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.TextView;
import com.jzn.simplemvprxjavaretrofit.common.BaseActivity;
import com.jzn.simplemvprxjavaretrofit.presenter.BookPresenter;
import com.jzn.simplemvprxjavaretrofit.utils.LogUtils;
import com.jzn.simplemvprxjavaretrofit.utils.ToastUtils;
import com.jzn.simplemvprxjavaretrofit.view.IBookView;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;

public class BookActivity extends BaseActivity implements IBookView {
    @Bind(R.id.tv_title_bookdetail)
    TextView tv_title_bookdetail;
    @Bind(R.id.tv_autor_bookdetail)
    TextView tv_autor_bookdetail;
    @Bind(R.id.tv_price_bookdetail)
    TextView tv_price_bookdetail;
    @Bind(R.id.tv_pages_bookdetail)
    TextView tv_pages_bookdetail;

    @Bind(R.id.tv_detail_bookdetail)
    TextView tv_detail_bookdetail;

    private BookPresenter mBookPresenter;
    @Bind(R.id.refresh_layout_main)
    SwipeRefreshLayout srl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        //获取数据
        mBookPresenter = new BookPresenter(this);
        mBookPresenter.getBookInfo("isbn/:9787121267734");
    }

    @Override
    public void init() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBookPresenter = new BookPresenter(BookActivity.this);
                mBookPresenter.getBookInfo("isbn/:9787121267734");
            }
        });
    }

    @Override
    public void showProgress() {
        setLoadingIndicator(true);
    }

    @Override
    public void hideProgress() {
        mBookPresenter.onUnsubscribe();
        setLoadingIndicator(false);

    }

    @Override
    public void loadBookInfo(String bookInfo) {
        LogUtils.d("BookInfo==" + bookInfo.toString());
        try {
            JSONObject jsonObject = new JSONObject(bookInfo);
            tv_title_bookdetail.setText(jsonObject.getString("title"));
            tv_autor_bookdetail.setText(jsonObject.getString("author"));
            tv_price_bookdetail.setText(jsonObject.getString("price"));
            tv_pages_bookdetail.setText(jsonObject.getString("pages"));
            tv_detail_bookdetail.setText(jsonObject.getString("summary"));
        } catch (JSONException e) {
            LogUtils.d(e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        srl.post(new Runnable() {
            @Override
            public void run() {
                //显示刷新框
                srl.setRefreshing(active);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        mBookPresenter.onUnsubscribe();
    }
}
