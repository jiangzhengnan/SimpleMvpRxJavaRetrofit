package com.jzn.simplemvprxjavaretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jzn.simplemvprxjavaretrofit.bean.BookInfo;
import com.jzn.simplemvprxjavaretrofit.common.BaseActivity;
import com.jzn.simplemvprxjavaretrofit.presenter.BookPresenter;
import com.jzn.simplemvprxjavaretrofit.utils.LogUtils;
import com.jzn.simplemvprxjavaretrofit.utils.ToastUtils;
import com.jzn.simplemvprxjavaretrofit.view.IBookView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookActivity extends BaseActivity implements IBookView{
    @Bind(R.id.tv_title_bookdetail)
    TextView tv_title_bookdetail;
    @Bind(R.id.tv_autor_bookdetail)
    TextView tv_autor_bookdetail;
    @Bind(R.id.tv_price_bookdetail)
    TextView tv_price_bookdetail;
    @Bind(R.id.tv_pages_bookdetail)
    TextView tv_pages_bookdetail;
    @Bind(R.id.btn_detail_bookdetail)
    TextView btn_detail_bookdetail;

    private BookPresenter mBookPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //获取数据
        mBookPresenter = new BookPresenter(this);
        mBookPresenter.getBookInfo("isbn/:9787121267734");
    }

    @Override
    public void showProgress() {
        ToastUtils.makeText(this,"加载中(此处应有进度条)");
    }

    @Override
    public void hideProgress() {
        mBookPresenter.onUnsubscribe();
    }

    @Override
    public void loadBookInfo(String bookInfo) {
        LogUtils.d("BookInfo==" + bookInfo.toString());
        try {
            JSONObject jsonObject = new JSONObject(bookInfo);
            tv_title_bookdetail.setText(jsonObject.getString("title"));
       //     tv_autor_bookdetail.setText(bookInfo.getAutor()[0]);
        //    tv_price_bookdetail.setText(bookInfo.getPrice());
         //   tv_pages_bookdetail.setText(bookInfo.getPages());
          //  btn_detail_bookdetail.setText(bookInfo.getDetail());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        mBookPresenter.onUnsubscribe();
    }
}
