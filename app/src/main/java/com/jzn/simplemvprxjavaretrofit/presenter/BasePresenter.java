package com.jzn.simplemvprxjavaretrofit.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jiangzn on 16/9/26.
 */
public class BasePresenter implements IPresenter {

    protected CompositeSubscription mCompositeSubscription;

    //RxJava取消注册防止内存泄漏
    @Override
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions())
            mCompositeSubscription.unsubscribe();
    }

    //RxJava注册
    @Override
    public void addSubscription(Subscription subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        if (subscriber!=null)
        mCompositeSubscription.add(subscriber);
    }
}
