package com.plattysoft.yass.movement;

import android.util.Log;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.BodyType;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;
import com.plattysoft.yass.engine.ScreenGameObject;
import com.plattysoft.yass.engine.Sprite;
import com.plattysoft.yass.sound.GameEvent;

/**
 * Created by Raul Portales on 12/03/15.
 */
public class Bullet extends Sprite {

    private double mSpeedFactor;

    private Player mParent;

    public Bullet(GameEngine gameEngine) {
        super(gameEngine, R.drawable.bullet, BodyType.Rectangular);
        mSpeedFactor = gameEngine.mPixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mY += mSpeedFactor * elapsedMillis;
        if (mY < -mHeight) {
            removeFromGameEngine(gameEngine);
        }
    }

    @Override
    public void onRemovedFromGameEngine() {
        mParent.releaseBullet(this);
    }

    public void init(Player parent, double positionX, double positionY) {
        mX = positionX - mWidth /2;
        mY = positionY - mHeight /2;
        mParent = parent;
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            // Remove both from the game (and return them to their pools)
            removeFromGameEngine(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            a.explode(gameEngine);
            a.removeFromGameEngine(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
            // Add some score
        }
    }
}
