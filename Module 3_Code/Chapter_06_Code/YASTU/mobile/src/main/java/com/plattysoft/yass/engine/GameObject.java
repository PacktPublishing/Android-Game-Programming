package com.plattysoft.yass.engine;

import android.graphics.Canvas;
import android.graphics.Rect;

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

    public void onPostUpdate(GameEngine gameEngine) {
    }

    public void addToGameEngine (GameEngine gameEngine, int layer) {
        gameEngine.addGameObject(this, layer);
    }

    public void removeFromGameEngine (GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
    }

    public void onAddedToGameEngine() {
    }

    public void onRemovedFromGameEngine() {
    }

}
