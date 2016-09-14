package com.plattysoft.yass.movement;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;
import com.plattysoft.yass.input.InputController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Portales on 10/03/15.
 */
public class Player extends GameObject {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 250;
    private final View mView;

    List<Bullet> mBullets = new ArrayList<Bullet>();

    private final ImageView mShip;
    private int mMaxX;
    private int mMaxY;

    private final TextView mTextView;

    double mPositionX;
    double mPositionY;

    double mSpeedFactor;
    private double mPixelFactor;
    private long mTimeSinceLastFire;

    public Player(final View view) {
        mView = view;
        // We read the size of the view
        mPixelFactor = view.getHeight() / 400d;
        mMaxX = view.getWidth() - view.getPaddingRight() - view.getPaddingRight();
        mMaxY = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
        mSpeedFactor = mPixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        // We create an image view and add it to the view
        mTextView = (TextView) view.findViewById(R.id.txt_score);

        mShip = new ImageView(view.getContext());
        Drawable shipDrawable = view.getContext().getResources().getDrawable(R.drawable.ship);

        mShip.setLayoutParams(new ViewGroup.LayoutParams(
                (int) (shipDrawable.getIntrinsicWidth() * mPixelFactor),
                (int) (shipDrawable.getIntrinsicHeight() * mPixelFactor)));
        mShip.setImageDrawable(shipDrawable);
        ((FrameLayout) view).addView(mShip);

        mMaxX -= (shipDrawable.getIntrinsicWidth()*mPixelFactor);
        mMaxY -= (shipDrawable.getIntrinsicHeight()*mPixelFactor);

        initBulletPool();
    }

    private void initBulletPool() {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            mBullets.add(new Bullet(mView, mPixelFactor));
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
            b.init(this, mPositionX + mShip.getWidth()/2, mPositionY);
            gameEngine.addGameObject(b);
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

    @Override
    public void onDraw() {
        mTextView.setText("["+(int) (mPositionX)+","+(int) (mPositionY)+"]");
        mShip.animate().translationX((int) mPositionX).translationY((int) mPositionY)
                .setDuration(1)
                .start();
    }
}