package com.sz.transformation.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sz.transformation.R;
import com.sz.transformation.util.SpeechRecognizerHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initSpeechRecognizer();
    }

    private void initSpeechRecognizer() {
        SpeechRecognizerHelper.init(this, new SpeechRecognizerHelper.OnDoneListener() {
            @Override
            public void done() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}