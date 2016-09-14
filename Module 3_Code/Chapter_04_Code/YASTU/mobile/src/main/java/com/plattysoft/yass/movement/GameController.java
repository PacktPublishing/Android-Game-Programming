package com.plattysoft.yass.movement;

import android.graphics.Canvas;
import android.view.View;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Portales on 23/03/15.
 */
public class GameController extends GameObject {

    private static final int NUM_WAVES = 3;
    private static final int TIME_BETWEEN_ENEMIES = 500;
    //    private final int sAsteroidDrawableArray = new int[] {
//            R.drawable.a10000,
//            R.drawable.a30000,
//            R.drawable.a40000
//    };
    private long mCurrentMillis;
    private List<Asteroid> mAsteroidPool = new ArrayList<Asteroid>();
//    private int[] mEnemiesSpawned = new int[NUM_WAVES];
    private int mEnemiesSpawned;
    private int[] mWaveStartingTimestamp = new int[] {
        0000,15000,20000
    };

    public GameController(GameEngine gameEngine) {
        double pixelFactor = gameEngine.mPixelFactor;
        // We initialize the pool of items now
        for (int i=0; i<10; i++) {
            mAsteroidPool.add(new Asteroid(this, gameEngine));
        }
    }

    @Override
    public void startGame() {
        mCurrentMillis = 0;
        mEnemiesSpawned = 0;
//        for (int i=0; i<NUM_WAVES; i++) {
//            mEnemiesSpawned[i] = 0;
//        }
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mCurrentMillis += elapsedMillis;
//        if (mAsteroidPool.isEmpty()) {
//            return;
//        }
//        // We span 3 waves of asteroids, each with 2 possible drawings
//        for (int i=0; i<NUM_WAVES; i++) {
//            if (mCurrentMillis > mWaveStartingTimestamp[i]) {
                long waveTimestamp = mEnemiesSpawned*TIME_BETWEEN_ENEMIES; // - mWaveStartingTimestamp[i];
                if (mCurrentMillis > waveTimestamp) {
                    // Spawn a new enemy
                    Asteroid a = mAsteroidPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a, mLayer);
                    mEnemiesSpawned++;
                    return;
                }
//                return;
//            }
//        }
        // Each wave takes 10 asteroids separated 500 ms (wave lasts for 5 seconds)
        // Asteroids should be faster than the spaceship, so 175 units/second
        // Between waves, we wait for 3 seconds
    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(Asteroid asteroid) {
        mAsteroidPool.add(asteroid);
    }
}
