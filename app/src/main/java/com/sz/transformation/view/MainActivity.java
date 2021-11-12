package com.sz.transformation.view;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.sz.transformation.R;
import com.sz.transformation.base.BaseActivity;
import com.sz.transformation.databinding.ActivityMainBinding;
import com.sz.transformation.util.Const;
import com.sz.transformation.util.ImageLoader;
import com.sz.transformation.util.SpeechRecognizerHelper;

import java.util.List;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> implements View.OnClickListener, RecognitionListener {

    private boolean isStateBarVisiable = true;
    private long mLastClickTransformTime;

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
        SpeechRecognizerHelper.init(this,this);
        requestPermission();
        switchState(getPresenter().getOpeningState());
        getBinding().ivBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isStateBarVisiable) {
                            hideSystemUI();
                        }
                        if (System.currentTimeMillis() - mLastClickTransformTime < 5 * 1000) {
                            Toast.makeText(getContext(), R.string.do_not_call_ultraman_too_often, Toast.LENGTH_SHORT).show();
                        } else {
                            mLastClickTransformTime = System.currentTimeMillis();
                            switchState(true);
                        }
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
                            isStateBarVisiable = true;
                        } else {
                            // The system bars are NOT visible.
                            isStateBarVisiable = false;
                        }
                    }
                });

    }

    private void requestPermission() {
        XXPermissions.with(this)
                .permission(Permission.RECORD_AUDIO)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {

                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        } else {
                            Toast.makeText(MainActivity.this, "麦克风权限获取失败", Toast.LENGTH_SHORT).show();
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
            Log.d("test", "开始记录语音");
            SpeechRecognizerHelper.getInstance().startListening(SpeechRecognizerHelper.MENU_SEARCH);
            getPresenter().playSound(MainPresenter.SOUND_TRANSFORM);
            ImageLoader.loadBigImage(R.drawable.pic_sparklence_on, getBinding().ivBg);
            getBinding().ivBg2.postDelayed(() -> {
                        Log.d("test", "停止记录，开始识别");
                        SpeechRecognizerHelper.getInstance().stop();
                    }
                    , 2000
            );
        } else {
            getPresenter().stopSound();
            ImageLoader.loadBigImage(R.drawable.pic_sparklence_off, getBinding().ivBg);
            getBinding().ivBg2.setImageDrawable(null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpeechRecognizerHelper.getInstance().cancel();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 语音识别
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {

    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        //最终结果
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Log.d("test", String.format("识别结果text===>%s", text));
            if (text.equals(Const.COMMAND_DIGA)) {
                ImageLoader.loadImageWithTransition(R.drawable.pic_sparklence_light_on, getBinding().ivBg2);
            }
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }
}
