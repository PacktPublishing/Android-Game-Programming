package com.plattysoft.yass.movement;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;
import com.plattysoft.yass.engine.Sprite;

import java.util.Random;

/**
 * Created by Raul Portales on 23/03/15.
 */
public class Asteroid extends Sprite {

    private final GameController mController;

    private final double mSpeed;
    private double mSpeedX;
    private double mSpeedY;
    private double mRotationSpeed;

    public Asteroid(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.a10000);
        mSpeed = 200d*mPixelFactor/1000d;
        mController = gameController;
    }

    @Override
    public void startGame() {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mPositionX += mSpeedX * elapsedMillis;
        mPositionY += mSpeedY * elapsedMillis;
        mRotation += mRotationSpeed * elapsedMillis;
        if (mRotation > 360) {
            mRotation = 0;
        }
        else if (mRotation < 0) {
            mRotation = 360;
        }
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (mPositionY > gameEngine.mHeight) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            mController.returnToPool(this);
        }
    }

    public void init(GameEngine gameEngine) {
        // They initialize in a [-30, 30] degrees angle
        double angle = gameEngine.mRandom.nextDouble()*Math.PI/3d-Math.PI/6d;
        mSpeedX = mSpeed * Math.sin(angle);
        mSpeedY = mSpeed * Math.cos(angle);
        // Asteroids initialize in the central 50% of the screen horizontally
        mPositionX = gameEngine.mRandom.nextInt(gameEngine.mWidth/2)+gameEngine.mWidth/4;
        // They initialize outside of the screen vertically
        mPositionY = -mImageHeight;
        mRotationSpeed = angle*(180d / Math.PI)/250d; // They rotate 4 times their ange in a second.
        mRotation = gameEngine.mRandom.nextInt(360);
    }
}
