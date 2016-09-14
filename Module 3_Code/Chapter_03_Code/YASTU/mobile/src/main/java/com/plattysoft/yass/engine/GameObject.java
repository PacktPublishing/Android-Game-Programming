package com.plattysoft.yass.engine;

import android.graphics.Canvas;

/**
 * Created by Raul Portales on 03/03/15.
 */
public abstract class GameObject {

    public int mLayer;

    public abstract void startGame();

    public abstract void onUpdate(long elapsedMillis, GameEngine gameEngine);

    public abstract void onDraw(Canvas canvas);

    public final Runnable mOnAddedRunnable = new Runnable() {
        @Override
        public void run() {
            onAddedToGameUiThread();
        }
    };

    public final Runnable mOnRemovedRunnable = new Runnable() {
        @Override
        public void run() {
            onRemovedFromGameUiThread();
        }
    };

    public void onRemovedFromGameUiThread(){
    }

    public void onAddedToGameUiThread(){
    }
}
