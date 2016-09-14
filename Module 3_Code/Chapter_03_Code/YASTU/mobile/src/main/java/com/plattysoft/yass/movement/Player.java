package com.plattysoft.yass.movement;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;
import com.plattysoft.yass.engine.Sprite;
import com.plattysoft.yass.input.InputController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Portales on 10/03/15.
 */
public class Player extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 250;

    List<Bullet> mBullets = new ArrayList<Bullet>();

    private int mMaxX;
    private int mMaxY;

    double mSpeedFactor;
    private long mTimeSinceLastFire;

    public Player(GameEngine gameEngine) {
        super(gameEngine, R.drawable.ship);
        // We read the size of the view
        mSpeedFactor = mPixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen

        mMaxX = gameEngine.mWidth - mImageWidth;
        mMaxY = gameEngine.mHeight - mImageHeight;

        initBulletPool(gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            mBullets.add(new Bullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (mBullets.isEmpty()) {
            return null;
        }
        return mBullets.remove(0);
    }

    void releaseBullet(Bullet b) {
        mBullets.add(b);
    }

    @Override
    public void startGame() {
        mPositionX = mMaxX / 2;
        mPositionY = mMaxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.mInputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (gameEngine.mInputController.mIsFiring && mTimeSinceLastFire > TIME_BETWEEN_BULLETS) {
            Bullet b = getBullet();
            if (b == null) {
                return;
            }
            b.init(this, mPositionX + mImageWidth/2, mPositionY);
            gameEngine.addGameObject(b, 1);
            mTimeSinceLastFire = 0;
        }
        else {
            mTimeSinceLastFire += elapsedMillis;
        }
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        mPositionX += mSpeedFactor * inputController.mHorizontalFactor * elapsedMillis;
        if (mPositionX < 0) {
            mPositionX = 0;
        }
        if (mPositionX > mMaxX) {
            mPositionX = mMaxX;
        }
        mPositionY += mSpeedFactor * inputController.mVerticalFactor * elapsedMillis;
        if (mPositionY < 0) {
            mPositionY = 0;
        }
        if (mPositionY > mMaxY) {
            mPositionY = mMaxY;
        }
    }
}