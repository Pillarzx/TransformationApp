package com.sz.transformation.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.sz.transformation.util.eventbus.EventBusUtil;

/**
 * @author Sean Zhang
 * Date 2021/8/14
 */
public abstract class BaseActivity<P extends BasePresenter,V extends ViewDataBinding> extends AppCompatActivity {
    private P mPresenter;
    private V mBinding;
    public abstract int initLayout();
    public abstract P initPresenter();
    public abstract void setView();

    public P getPresenter(){
        if (mPresenter==null){
            throw new UnsupportedOperationException("请先实现方法 \"initPresenter()\"，用于设置一个Presenter。");
        }
        return mPresenter;
    }
    public V getBinding(){
        return mBinding;
    }
    public Context getContext(){
        return this;
    }
    public boolean isRegisterEventBus(){
        return false;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=DataBindingUtil.setContentView(this,initLayout());
        mPresenter=initPresenter();
        if (mPresenter!=null){
            mPresenter.onAttachView(this);
            setView();
            mPresenter.onCreate();
        }else {
            setView();
        }
        if (isRegisterEventBus()){
            EventBusUtil.register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
