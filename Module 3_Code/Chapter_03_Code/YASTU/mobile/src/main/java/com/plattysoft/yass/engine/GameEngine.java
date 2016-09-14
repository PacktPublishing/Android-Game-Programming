package com.plattysoft.yass.engine;

import android.app.Activity;
import android.content.Context;

import com.plattysoft.yass.input.BasicInputController;
import com.plattysoft.yass.input.InputController;
import com.plattysoft.yass.movement.Bullet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Raul Portales on 02/03/15.
 */
public class GameEngine {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 5;
    private final GameView mGameView;

    private List<List<GameObject>> mLayers = new ArrayList<List<GameObject>>();
    private List<GameObject> mGameObjects = new ArrayList<GameObject>();

    private List<GameObject> mObjectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToRemove = new ArrayList<GameObject>();

    private Map<Class, List<GameObject>> mPools = new HashMap<Class, List<GameObject>>();

    private UpdateThread mUpdateThread;
    private DrawThread mDrawThread;

    public Activity mActivity;

    public InputController mInputController;

    public Random mRandom = new Random();

    public int mWidth;
    public int mHeight;
    public double mPixelFactor;

    public void setInputController(InputController controller) {
        mInputController = controller;
    }

    public GameEngine (Activity a, GameView gameView, int numLayers) {
        mActivity = a;
        mGameView = gameView;
        mGameView.setGameObjects(mLayers);
        initPools();

        mWidth = gameView.getWidth() - gameView.getPaddingRight() - gameView.getPaddingLeft();
        mHeight = gameView.getHeight() - gameView.getPaddingTop() - gameView.getPaddingBottom();

        mPixelFactor = mHeight / 400d;

        for (int i=0; i<numLayers; i++) {
            mLayers.add(new ArrayList<GameObject>());
        }
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
        int numLayers = mGameObjects.size();
        for (int i=0; i<numLayers; i++) {
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

    public void addGameObject(final GameObject gameObject, int layer) {
        gameObject.mLayer = layer;
        if (isRunning()){
            mObjectsToAdd.add(gameObject);
        }
        else {
            addToLayerNow(gameObject);
        }
        mActivity.runOnUiThread(gameObject.mOnAddedRunnable);
    }

    public void removeGameObject(final GameObject gameObject) {
        mObjectsToRemove.add(gameObject);
        mActivity.runOnUiThread(gameObject.mOnRemovedRunnable);
    }

    public void onUpdate(long elapsedMillis) {
        mInputController.onPreUpdate();
        int numLayers = mGameObjects.size();
        for (int i=0; i<numLayers; i++) {
            mGameObjects.get(i).onUpdate(elapsedMillis, this);
        }
        synchronized (mLayers) {
            while (!mObjectsToRemove.isEmpty()) {
                GameObject objectToRemove = mObjectsToRemove.remove(0);
                mGameObjects.remove(objectToRemove);
                mLayers.get(objectToRemove.mLayer).remove(objectToRemove);
            }
            while (!mObjectsToAdd.isEmpty()) {
                GameObject gameObject = mObjectsToAdd.remove(0);
                addToLayerNow(gameObject);
            }
        }
    }

    private void addToLayerNow (GameObject object) {
        int layer = object.mLayer;
        while (mLayers.size() <= layer) {
            mLayers.add(new ArrayList<GameObject>());
        }
        mLayers.get(layer).add(object);
        mGameObjects.add(object);
    }

    public void onDraw() {
        mGameView.draw();
    }

    public boolean isRunning() {
        return mUpdateThread != null && mUpdateThread.isGameRunning();
    }

    public boolean isPaused() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }

    public Context getContext() {
        return mGameView.getContext();
    }
}
