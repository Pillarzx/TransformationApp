package com.sz.transformation.util;

import android.content.Context;
import android.os.AsyncTask;
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
    private static final String KWS_SEARCH = "wakeup";
    private static OnDoneListener onDoneListener;

    private SpeechRecognizerHelper(Context context) {
        mContext = new WeakReference<>(context);
        new SetTask(mContext.get(),onDoneListener).execute();
    }

    private static class SetTask extends AsyncTask<Void, Void, Exception> {
        private final WeakReference<Context> imContext;
        private final OnDoneListener iOnDoneListener;

        public SetTask(Context context,OnDoneListener listener){
            imContext=new WeakReference<>(context);
            iOnDoneListener =listener;
        }
        @Override
        protected Exception doInBackground(Void... voids) {
            try {
                Assets assets = new Assets(imContext.get());
                File assetDir = assets.syncAssets();
                SpeechRecognizerHelper.getInstance().setupRecognizer(assetDir);
            } catch (IOException e) {
                e.printStackTrace();
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception e) {
            Log.d("test", "SpeechRecognizer加载完毕");
            iOnDoneListener.done();
        }
    }

    public void setOnDoneListener(OnDoneListener listener) {
        this.onDoneListener = listener;
    }

    public static void init(Context context,OnDoneListener listener) {
        onDoneListener=listener;
        if (instance == null) {
            instance = new SpeechRecognizerHelper(context);
        }
    }

    public static SpeechRecognizerHelper getInstance() {
        return instance;
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        mRecognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "ptm-zh"))
                .setDictionary(new File(assetsDir, "ptm-zh/0140.dic"))
                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .getRecognizer();

        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
//        mRecognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        mRecognizer.addNgramSearch(KWS_SEARCH,new File(assetsDir, "ptm-zh/0140.lm"));
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

    public interface OnDoneListener {
        void done();
    }
}
