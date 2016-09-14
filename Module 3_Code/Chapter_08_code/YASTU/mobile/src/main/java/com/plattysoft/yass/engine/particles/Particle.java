package com.plattysoft.yass.engine.particles;

import com.plattysoft.yass.engine.BodyType;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.Sprite;

import java.util.ArrayList;

/**
 * Created by Raul Portales on 02/04/15.
 */
public class Particle extends Sprite {

    private final ParticleSystem mParent;

    private long mTimeToLive;

    public double mSpeedX;
    public double mSpeedY;

    public double mRotationSpeed;

    private ArrayList<ParticleModifier> mModifiers;
    private long mTotalMillis;

    protected Particle(ParticleSystem particleSystem, GameEngine gameEngine, int drawableRes) {
        super(gameEngine, drawableRes, BodyType.None);
        mParent = particleSystem;
    }

    @Override
    public void startGame(GameEngine gameEngine) {

    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
        mParent.returnToPool(this);
    }

    public void activate(GameEngine gameEngine, long timeToLive, double x, double y, ArrayList<ParticleModifier> modifiers, int layer) {
        mTimeToLive = timeToLive;
        mX = x-mWidth/2;
        mY = y-mHeight/2;
        addToGameEngine(gameEngine, layer);
        mModifiers = modifiers;
        mTotalMillis = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mTotalMillis += elapsedMillis;
        if (mTotalMillis > mTimeToLive) {
            // Return it to the pool
            removeFromGameEngine(gameEngine);
        }
        else {
            mX += mSpeedX*elapsedMillis;
            mY += mSpeedY*elapsedMillis;
            mRotation += mRotationSpeed*elapsedMillis/1000d;
            for (int i=0; i<mModifiers.size(); i++) {
                mModifiers.get(i).apply(this, mTotalMillis);
            }
        }
    }
}
