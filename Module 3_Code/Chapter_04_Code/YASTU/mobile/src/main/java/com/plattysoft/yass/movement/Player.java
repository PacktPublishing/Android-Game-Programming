package com.plattysoft.yass.movement;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.BodyType;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;
import com.plattysoft.yass.engine.ScreenGameObject;
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
        super(gameEngine, R.drawable.ship, BodyType.Circular);
        // We read the size of the view
        mSpeedFactor = mPixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen

        mMaxX = gameEngine.mWidth - mWidth;
        mMaxY = gameEngine.mHeight - mHeight;

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
        mX = mMaxX / 2;
        mY = mMaxY / 2;
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
            b.init(this, mX + mWidth /2, mY);
            gameEngine.addGameObject(b, 1);
            mTimeSinceLastFire = 0;
        }
        else {
            mTimeSinceLastFire += elapsedMillis;
        }
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        mX += mSpeedFactor * inputController.mHorizontalFactor * elapsedMillis;
        if (mX < 0) {
            mX = 0;
        }
        if (mX > mMaxX) {
            mX = mMaxX;
        }
        mY += mSpeedFactor * inputController.mVerticalFactor * elapsedMillis;
        if (mY < 0) {
            mY = 0;
        }
        if (mY > mMaxY) {
            mY = mMaxY;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            gameEngine.removeGameObject(this);
            //gameEngine.stopGame();
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
        }
    }
}