package com.sz.transformation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sz.transformation.R;
import com.sz.transformation.base.BaseActivity;
import com.sz.transformation.base.BasePresenter;
import com.sz.transformation.databinding.ActivityMainBinding;
import com.sz.transformation.util.ImageLoader;

import java.util.HashMap;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> implements View.OnClickListener {

    private boolean isStateBarVisiable = true;


    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    public void setView() {

        switchState(getPresenter().getOpeningState());
        getBinding().ivBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isStateBarVisiable) {
                            hideSystemUI();
                        }
                        switchState(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        switchState(false);
                        break;
                    default:
                        break;
                }
                return true;
            }

        });

        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            //The system bars are visible.
                            isStateBarVisiable=true;
                        } else {
                            // The system bars are NOT visible.
                            isStateBarVisiable=false;
                        }
                    }
                });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        hideSystemUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bg:
                switchState(!getPresenter().getOpeningState());
                break;
            default:
                break;
        }
    }

    public void switchState(boolean isOpen) {
        if (isOpen) {
            getPresenter().playSound(MainPresenter.SOUND_TRANSFORM);
            ImageLoader.loadBigImage(R.drawable.pic_sparklence_on, getBinding().ivBg);
        } else {
            getPresenter().stopSound();
            ImageLoader.loadBigImage(R.drawable.pic_sparklence_off, getBinding().ivBg);
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
