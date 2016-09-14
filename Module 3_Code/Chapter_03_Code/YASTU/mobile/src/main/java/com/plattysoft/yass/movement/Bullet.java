package com.plattysoft.yass.movement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;
import com.plattysoft.yass.engine.Sprite;

/**
 * Created by Raul Portales on 12/03/15.
 */
public class Bullet extends Sprite {

    private double mSpeedFactor;

    private Player mParent;

    public Bullet(GameEngine gameEngine) {
        super(gameEngine, R.drawable.bullet);
        mSpeedFactor = gameEngine.mPixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mPositionY += mSpeedFactor * elapsedMillis;
        if (mPositionY < -mImageHeight) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            mParent.releaseBullet(this);
        }
    }

    public void init(Player parent, double positionX, double positionY) {
        mPositionX = positionX - mImageWidth/2;
        mPositionY = positionY - mImageHeight/2;
        mParent = parent;
    }
}
