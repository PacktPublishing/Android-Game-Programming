package com.plattysoft.yass.movement;

import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.BodyType;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.ScreenGameObject;
import com.plattysoft.yass.engine.Sprite;
import com.plattysoft.yass.engine.particles.ParticleSystem;
import com.plattysoft.yass.input.InputController;
import com.plattysoft.yass.sound.GameEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Portales on 10/03/15.
 */
public class Player extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 250;
    private static final int EXPLOSION_PARTICLES = 20;

    private final ParticleSystem mEngineFireParticle;
    private final ParticleSystem mExplosionParticleSystem1;
    private final ParticleSystem mExplosionParticleSystem2;

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
        mEngineFireParticle = new ParticleSystem(gameEngine, 50, R.drawable.particle_smoke, 600)
                .setRotationSpeedRange(-30, 30)
                .setSpeedModuleAndAngleRange(50, 80, 60, 120)
                .setInitialRotationRange(0, 360)
                .setFadeOut(400);

        mExplosionParticleSystem1 = new ParticleSystem(gameEngine, EXPLOSION_PARTICLES, R.drawable.particle_ship_explosion_1, 600)
                .setSpeedRange(30, 150)
                .setInitialRotationRange(0,360)
                .setFadeOut(200);
        mExplosionParticleSystem2 = new ParticleSystem(gameEngine, EXPLOSION_PARTICLES, R.drawable.particle_ship_explosion_2, 600)
                .setSpeedRange(30, 150)
                .setInitialRotationRange(0, 360)
                .setFadeOut(200);
    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
        mEngineFireParticle.removeFromGameEngine(gameEngine);
    }

    @Override
    public void addToGameEngine(GameEngine gameEngine, int layer) {
        super.addToGameEngine(gameEngine, layer);
        mEngineFireParticle.addToGameEngine(gameEngine, mLayer - 1);
        mEngineFireParticle.emit(12);
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
    public void startGame(GameEngine gameEngine) {
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
            b.init(this, mX + mWidth / 2, mY);
            b.addToGameEngine(gameEngine, 1);
            mTimeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
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
        mEngineFireParticle.setPosition(mX+mWidth/2, mY+mHeight);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            removeFromGameEngine(gameEngine);
            //gameEngine.stopGame();
            Asteroid a = (Asteroid) otherObject;
            a.removeFromGameEngine(gameEngine);
            mEngineFireParticle.stopEmiting();
            mExplosionParticleSystem1.oneShot(gameEngine, mX + mWidth / 2, mY + mWidth / 2, EXPLOSION_PARTICLES);
            mExplosionParticleSystem2.oneShot(gameEngine, mX + mWidth / 2, mY + mWidth / 2, EXPLOSION_PARTICLES);
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }
    }
}