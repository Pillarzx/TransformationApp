package com.sz.transformation.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ldoublem.loadingviewlib.view.LVBlazeWood;
import com.sz.transformation.R;
import com.sz.transformation.databinding.ActivitySplashBinding;
import com.sz.transformation.util.SpeechRecognizerHelper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding mBinding;
    private LVBlazeWood mLvBlazeWood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSpeechRecognizer();
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_splash);
        mLvBlazeWood=mBinding.lvBlazewood;
        mLvBlazeWood.startAnim();
    }

    private void initSpeechRecognizer() {
        SpeechRecognizerHelper.init(this, new SpeechRecognizerHelper.OnDoneListener() {
            @Override
            public void done() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                if (mLvBlazeWood!=null) {
                    mLvBlazeWood.stopAnim();
                }
            }
        });
    }

}