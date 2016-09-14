package com.plattysoft.yass.engine;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Portales on 02/03/15.
 */
public class GameEngine {

    private List<GameObject> mGameObjects = new ArrayList<GameObject>();

    private UpdateThread mUpdateThread;
    private DrawThread mDrawThread;

    private Runnable mDrawRunnable = new Runnable() {
        @Override
        public void run() {
            int numGameObjects = mGameObjects.size();
            for (int i = 0; i < numGameObjects; i++) {
                mGameObjects.get(i).onDraw();
            }
        }
    };

    private Activity mActivity;

    public GameEngine (Activity a) {
        mActivity = a;
    }

    public void startGame() {
        // Stop a game if it is running
        stopGame();

        // Setup the game objects
        int numGameObjects = mGameObjects.size();
        for (int i=0; i<numGameObjects; i++) {
            mGameObjects.get(i).startGame();
        }

        // Start the update thread
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();

        // Start the drawing thread
        mDrawThread = new DrawThread(this);
        mDrawThread.start();
    }

    public void stopGame() {
        if (mUpdateThread != null) {
            mUpdateThread.stopGame();
        }
        if (mDrawThread != null) {
            mDrawThread.stopGame();
        }
    }

    public void pauseGame() {
        if (mUpdateThread != null) {
            mUpdateThread.pauseGame();
        }
        if (mDrawThread != null) {
            mDrawThread.pauseGame();
        }
    }

    public void resumeGame() {
        if (mUpdateThread != null) {
            mUpdateThread.resumeGame();
        }
        if (mDrawThread != null) {
            mDrawThread.resumeGame();
        }
    }

    public void addGameObject(GameObject gameObject) {
        mGameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        mGameObjects.remove(gameObject);
    }

    public void onUpdate(long elapsedMillis) {
        int numGameObjects = mGameObjects.size();
        for (int i=0; i<numGameObjects; i++) {
            mGameObjects.get(i).onUpdate(elapsedMillis, this);
        }
    }

    public void onDraw() {
        mActivity.runOnUiThread(mDrawRunnable);
    }

    public boolean isRunning() {
        return mUpdateThread != null && mUpdateThread.isGameRunning();
    }

    public boolean isPaused() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }
}
