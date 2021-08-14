package com.sz.transformation.base;

import java.lang.ref.WeakReference;

/**
 * @author Sean Zhang
 * Date 2021/8/14
 */
public abstract class BasePresenter<T> {
    private WeakReference<T> mView;

    protected void onCreate(){

    }
    void onAttachView(T view){
        mView=new WeakReference<>(view);
    }
    void onDetachView(){
        mView.clear();
    }
    public T getView(){
        return mView.get();
    }
}
