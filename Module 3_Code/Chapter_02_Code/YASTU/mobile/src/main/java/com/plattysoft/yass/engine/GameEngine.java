package com.plattysoft.yass.engine;

import android.app.Activity;

import com.plattysoft.yass.input.BasicInputController;
import com.plattysoft.yass.input.InputController;
import com.plattysoft.yass.movement.Bullet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Raul Portales on 02/03/15.
 */
public class GameEngine {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 5;

    private List<GameObject> mGameObjects = new ArrayList<GameObject>();

    private List<GameObject> mObjectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToRemove = new ArrayList<GameObject>();

    private Map<Class, List<GameObject>> mPools = new HashMap<Class, List<GameObject>>();

    private UpdateThread mUpdateThread;
    private DrawThread mDrawThread;

    private Runnable mDrawRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (mGameObjects) {
                int numGameObjects = mGameObjects.size();
                for (int i = 0; i < numGameObjects; i++) {
                    mGameObjects.get(i).onDraw();
                }
            }
        }
    };

    private Activity mActivity;

    public InputController mInputController;

    public void setInputController(InputController controller) {
        mInputController = controller;
    }

    public GameEngine (Activity a) {
        mActivity = a;
        initPools();
    }

    private void initPools() {
//        List<Bullet> bullets = new ArrayList<Bullet>();
//        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
//            bullets.add(new Bullet(getV, mPixelFactor));
//        }
    }

    public void startGame() {
        // Stop a game if it is running
        stopGame();

        // Setup the game objects
        int numGameObjects = mGameObjects.size();
        for (int i=0; i<numGameObjects; i++) {
            mGameObjects.get(i).startGame();
        }

        if (mInputController != null) {
            mInputController.onStart();
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
        if (mInputController != null) {
            mInputController.onStop();
        }
    }

    public void pauseGame() {
        if (mUpdateThread != null) {
            mUpdateThread.pauseGame();
        }
        if (mDrawThread != null) {
            mDrawThread.pauseGame();
        }
        if (mInputController != null) {
            mInputController.onPause();
        }
    }

    public void resumeGame() {
        if (mUpdateThread != null) {
            mUpdateThread.resumeGame();
        }
        if (mDrawThread != null) {
            mDrawThread.resumeGame();
        }
        if (mInputController != null) {
            mInputController.onResume();
        }
    }

    public void addGameObject(final GameObject gameObject) {
        if (isRunning()){
            mObjectsToAdd.add(gameObject);
        }
        else {
            mGameObjects.add(gameObject);
        }
        mActivity.runOnUiThread(gameObject.mOnAddedRunnable);
    }

    public void removeGameObject(final GameObject gameObject) {
        mObjectsToRemove.add(gameObject);
        mActivity.runOnUiThread(gameObject.mOnRemovedRunnable);
    }

    public void onUpdate(long elapsedMillis) {
        mInputController.onPreUpdate();
        int numGameObjects = mGameObjects.size();
        for (int i=0; i<numGameObjects; i++) {
            mGameObjects.get(i).onUpdate(elapsedMillis, this);
        }
        synchronized (mGameObjects) {
            while (!mObjectsToRemove.isEmpty()) {
                mGameObjects.remove(mObjectsToRemove.remove(0));
            }
            while (!mObjectsToAdd.isEmpty()) {
                mGameObjects.add(mObjectsToAdd.remove(0));
            }
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
