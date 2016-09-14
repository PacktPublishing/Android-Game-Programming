package com.plattysoft.yass.movement;

import android.util.Log;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.BodyType;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.Sprite;
import com.plattysoft.yass.engine.particles.ParticleSystem;
import com.plattysoft.yass.engine.particles.ScaleModifier;

/**
 * Created by Raul Portales on 23/03/15.
 */
public class Asteroid extends Sprite {

    public static final int EXPLOSION_PARTICLES = 15;
    private final GameController mController;

    private final double mSpeed;
    private double mSpeedX;
    private double mSpeedY;
    private double mRotationSpeed;
    private ParticleSystem mTrailParticleSystem;
    private ParticleSystem mExplisionParticleSystem;

    public Asteroid(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.a10000, BodyType.Circular);
        mSpeed = 200d*mPixelFactor/1000d;
        mController = gameController;
        mTrailParticleSystem = new ParticleSystem(gameEngine, 50, R.drawable.particle_dust, 600)
                .addModifier(new ScaleModifier(1, 2, 200, 600))
                .setFadeOut(200);
        mExplisionParticleSystem = new ParticleSystem(gameEngine, EXPLOSION_PARTICLES, R.drawable.particle_asteroid_1, 700)
                .setSpeedRange(15, 40)
                .setFadeOut(300)
                .setInitialRotationRange(0, 360)
                .setRotationSpeedRange(-180, 180);
    }

    @Override
    public void startGame() {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mX += mSpeedX * elapsedMillis;
        mY += mSpeedY * elapsedMillis;
        mRotation += mRotationSpeed * elapsedMillis;
        if (mRotation > 360) {
            mRotation = 0;
        }
        else if (mRotation < 0) {
            mRotation = 360;
        }
        mTrailParticleSystem.setPosition(mX + mWidth / 2, mY + mHeight / 2);
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (mY > gameEngine.mHeight) {
            removeFromGameEngine(gameEngine);
        }
    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
        mTrailParticleSystem.stopEmiting();
        mTrailParticleSystem.removeFromGameEngine(gameEngine);
    }

    @Override
    public void onRemovedFromGameEngine() {
        mController.returnToPool(this);
    }

    public void explode(GameEngine gameEngine) {
        mExplisionParticleSystem.oneShot(gameEngine, mX + mWidth / 2, mY + mHeight / 2, EXPLOSION_PARTICLES);
    }

    public void init(GameEngine gameEngine) {
        // They initialize in a [-30, 30] degrees angle
        double angle = gameEngine.mRandom.nextDouble()*Math.PI/3d-Math.PI/6d;
        mSpeedX = mSpeed * Math.sin(angle);
        mSpeedY = mSpeed * Math.cos(angle);

        int perpendicularAngleInDegrees = (int) (angle*180/Math.PI)+90;
//        mTrailParticleSystem.setSpeedByComponentsRange(-mSpeedY*2, mSpeedY*2, -mSpeedX*2, mSpeedX*2);
        // Asteroids initialize in the central 50% of the screen horizontally
        mX = gameEngine.mRandom.nextInt(gameEngine.mWidth / 2)+gameEngine.mWidth/4;
        // They initialize outside of the screen vertically
        mY = -mHeight;
        mRotationSpeed = angle*(180d / Math.PI)/250d; // They rotate 4 times their ange in a second.
        mRotation = gameEngine.mRandom.nextInt(360);

        mTrailParticleSystem.clearInitializers()
                .setInitialRotationRange(0,360)
                .setRotationSpeedRange(mRotationSpeed * 800, mRotationSpeed * 1000)
                .setSpeedByComponentsRange(-mSpeedY * 100, mSpeedY * 100, mSpeedX * 100, mSpeedX * 100);
    }

    @Override
    public void addToGameEngine (GameEngine gameEngine, int layer) {
        super.addToGameEngine(gameEngine, layer);
        mTrailParticleSystem.addToGameEngine(gameEngine, mLayer-1);
        mTrailParticleSystem.emit(15);
    }
}
