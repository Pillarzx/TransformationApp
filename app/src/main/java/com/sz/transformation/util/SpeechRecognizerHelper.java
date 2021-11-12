package com.sz.transformation.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

/**
 * @author Sean Zhang
 * Date: 2021/11/12
 */
public class SpeechRecognizerHelper {
    private static SpeechRecognizerHelper instance;
    private SpeechRecognizer mRecognizer;
    private WeakReference<Context> mContext;
    public static final String MENU_SEARCH = "menu";

    private SpeechRecognizerHelper(Context context,RecognitionListener listener) {
        mContext = new WeakReference<>(context);
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Assets assets = new Assets(mContext.get());
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir,listener);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void init(Context context,RecognitionListener listener) {
        if (instance == null) {
            instance = new SpeechRecognizerHelper(context,listener);
        }
    }

    public static SpeechRecognizerHelper getInstance() {
        return instance;
    }

    private void setupRecognizer(File assetsDir,RecognitionListener listener) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        mRecognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "ptm-zh"))
                .setDictionary(new File(assetsDir, "ptm-zh/0140.dic"))
                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .getRecognizer();
        Log.d("test","SpeechRecognizer加载完毕");
        mRecognizer.addListener(listener);
        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
//        mRecognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        mRecognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

    }

    public SpeechRecognizerHelper addListener(RecognitionListener listener) {
        mRecognizer.addListener(listener);
        return this;
    }

    public SpeechRecognizerHelper startListening(String searchName) {
        mRecognizer.startListening(searchName, 5000);
        return this;
    }

    public SpeechRecognizerHelper stop() {
        mRecognizer.stop();
        return this;
    }

    public void cancel() {
        if (mRecognizer != null) {
            mRecognizer.cancel();
            mRecognizer.shutdown();
        }
    }
}
