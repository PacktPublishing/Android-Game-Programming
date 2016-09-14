package com.plattysoft.yass.sound;

import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

public class SoundInfo {

	private static final long FLOOD_TIMEOUT = 500;
	
	private static final Random sRandom = new Random();
	
	private int[] mSoundId;
	private long mLastPlayTime;

	public SoundInfo(Context context, SoundPool engine, String[] filename) {	   
		mSoundId = new int [filename.length];
		for (int i=0; i<filename.length; i++) {
			try {
				mSoundId[i] = engine.load(context.getAssets().openFd("music/"+filename[i]), 1);
			} catch (IOException e) {
				Log.e("SoundInfo", "Sound not found: "+filename[i]);
				e.printStackTrace();
			}			
		}
	}

	public void play(SoundPool engineSound) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - mLastPlayTime > FLOOD_TIMEOUT) {
			mLastPlayTime = currentTime;
			int posToPlay = sRandom.nextInt(mSoundId.length);
			engineSound.play(mSoundId[posToPlay], 1.0f, 1.0f, 0, 0, 1.0f);
		}
	}

}
