package com.sz.transformation.view;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.sz.transformation.R;
import com.sz.transformation.base.BasePresenter;

import java.util.HashMap;

/**
 * @author Sean Zhang
 * Date 2021/8/14
 */
public class MainPresenter extends BasePresenter<MainActivity> {
    public static int SOUND_TRANSFORM = 1;
    private int tempSoundId=0;
    private boolean openingState = false;//变声器状态
    private SoundPool mSoundPool;
    private final HashMap<Integer, Integer> mSoundId = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        mSoundId.put(SOUND_TRANSFORM, mSoundPool.load(getView(), R.raw.sd_transform, 1));

    }

    public void playSound(int soundId) {
        if (mSoundId.get(soundId) != null) {
            tempSoundId=mSoundPool.play(mSoundId.get(soundId), 1, 1, 1, 0, 1);
            Log.d("test","playSound tempSoundId===="+tempSoundId);
        }
    }

    public void stopSound() {
        if (mSoundId.get(soundId) != null) {
            mSoundPool.stop(tempSoundId);
            tempSoundId=0;
        }
    }

    public boolean getOpeningState() {
        return openingState;
    }

    public void setOpeningState(boolean openingState) {
        this.openingState = openingState;
    }
}
