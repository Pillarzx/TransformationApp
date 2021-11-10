package com.sz.transformation.util;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huawei.hiai.asr.AsrConstants;
import com.huawei.hiai.asr.AsrListener;
import com.huawei.hiai.asr.AsrRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Sean Zhang
 * Date: 2021/11/9
 */
public class SpeechRecognition {
    private static final String TAG="SpeechRecognition";
    private static volatile SpeechRecognition instance;
    private AsrRecognizer mAsrRecognizer;
    private AsrListener mAsrListener;

    private SpeechRecognition(Context context,AsrListener listener) {
        mAsrRecognizer = AsrRecognizer.createAsrRecognizer(context);//创建引擎
        /*配置语音的输入来源:
        ASR_SRC_TYPE_FILE——语音输入来自文件；
        ASR_SRC_TYPE_RECORD——语音输入来自mic录入；
        ASR_SRC_TYPE_PCM——语音输入来自PCM数据流；
        */
        Intent initIntent = new Intent(); //初始化引擎
        /*设置VAD（Voice Activity Detection）时间
        ASR_VAD_END_WAIT_MS后置VAD时间，检查到说话停止时间达到vad时间，识别停止
        ASR_VAD_FRONT_WAIT_MS前置VAD时间，检查识别开始后没有说话达到vad时间，识别停止
        */
        initIntent.putExtra(AsrConstants.ASR_VAD_END_WAIT_MS, 2000);
        initIntent.putExtra(AsrConstants.ASR_VAD_FRONT_WAIT_MS, 4000);
        initIntent.putExtra(AsrConstants.ASR_AUDIO_SRC_TYPE, AsrConstants.ASR_SRC_TYPE_RECORD);
        mAsrRecognizer.init(initIntent , listener);

        ArrayList<String> nameList = new ArrayList<>();
        JSONObject data = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("迪迦");
        jsonArray.put("测试");
        try {
            data.put(AsrConstants.ASR_LEXICON_NAME_OTHERS, jsonArray);
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception");
        }
        nameList.add(AsrConstants.ASR_LEXICON_NAME_OTHERS); // 热词名称类型列表添加其他类型热词
        Intent updateIntent = new Intent();
        updateIntent.putExtra(AsrConstants.ASR_LEXICON_NAME, nameList); // 添加热词名称类型列表
        updateIntent.putExtra(AsrConstants.ASR_LEXICON_ITEMS, data.toString()); // 添加热词数据列表
        mAsrRecognizer.updateLexicon(updateIntent); //调用更新热词方法
    }
    public static void init(Context context,AsrListener listener){
        if (instance == null) {
            synchronized (SpeechRecognition.class) {
                if (instance == null) {
                    instance = new SpeechRecognition(context,listener);
                }
            }
        }
    }

    public static SpeechRecognition getInstance() {
        return instance;
    }

    /**
     * 开始识别
     */
    public void startListening(){
        Intent intent = new Intent();
        intent.putExtra(AsrConstants.ASR_VAD_FRONT_WAIT_MS, 4000);//设置前端静音检测时间
        intent.putExtra(AsrConstants.ASR_VAD_END_WAIT_MS, 5000); //设置后端静音检测时间
        intent.putExtra(AsrConstants.ASR_TIMEOUT_THRESHOLD_MS, 20000);//设置超时时间
        mAsrRecognizer.startListening(intent);
    }

    /**
     * 停止识别，希望获取识别结果时调用
     */
    public void stopListening(){
        mAsrRecognizer.stopListening();//停止识别
    }

    /**
     * 取消识别，识别取消且没有最终识别结果返回
     */
    public void cancel(){
        mAsrRecognizer.cancel();//取消识别
    }

    /**
     * 销毁
     */
    public void destroy(){
        mAsrRecognizer.destroy();
    }
}
