package com.plattysoft.yass.engine.particles;

import android.graphics.Canvas;

import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.ScreenGameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Raul Portales on 02/04/15.
 */
public class ParticleSystem extends ScreenGameObject {

    private final Random mRandom;
    private final ArrayList<ParticleModifier> mModifiers;
    private final ArrayList<ParticleInitializer> mInitializers;
    private final long mTimeToLive;

    private final double mPixelFactor;

    private List<Particle> mParticlePool = new ArrayList<Particle>();

    private long mTotalMillis;
    
    private int mActivatedParticles;
    private double mParticlesPerMilisecond;
    private boolean mIsEmiting;

    public ParticleSystem(GameEngine gameEngine, int maxParticles, int drawableRedId, long timeToLive) {
        mRandom = new Random();

        mModifiers = new ArrayList<ParticleModifier>();
        mInitializers = new ArrayList<ParticleInitializer>();

        mTimeToLive = timeToLive;

        mPixelFactor = gameEngine.mPixelFactor;
        for (int i=0; i<maxParticles; i++) {
            mParticlePool.add(new Particle(this, gameEngine, drawableRedId));
        }
    }

    public ParticleSystem setSpeedRange(double speedMin, double speedMax) {
        mInitializers.add(new SpeeddModuleAndRangeInitializer(speedMin*mPixelFactor, speedMax*mPixelFactor, 0, 360));
        return this;
    }

    public ParticleSystem setSpeedModuleAndAngleRange(double speedMin, double speedMax, int minAngle, int maxAngle) {
        mInitializers.add(new SpeeddModuleAndRangeInitializer(speedMin*mPixelFactor, speedMax*mPixelFactor, minAngle, maxAngle));
        return this;
    }

    public ParticleSystem setSpeedByComponentsRange(double speedMinX, double speedMaxX, double speedMinY, double speedMaxY) {
        mInitializers.add(new SpeeddByComponentsInitializer(speedMinX*mPixelFactor, speedMaxX*mPixelFactor,
                speedMinY*mPixelFactor, speedMaxY*mPixelFactor));
        return this;
    }

    public ParticleSystem setFadeOut(long milisecondsBeforeEnd) {
        mModifiers.add(new AlphaModifier(255, 0, mTimeToLive-milisecondsBeforeEnd, mTimeToLive));
        return this;
    }

    public ParticleSystem addModifier(ParticleModifier modifier) {
        mModifiers.add(modifier);
        return this;
    }

    public ParticleSystem setInitialRotationRange (int minAngle, int maxAngle) {
        mInitializers.add(new RotationInitiazer(minAngle, maxAngle));
        return this;
    }

    public ParticleSystem setRotationSpeedRange(double minRotationSpeed, double maxRotationSpeed) {
        mInitializers.add(new RotationSpeedInitializer(minRotationSpeed, maxRotationSpeed));
        return this;
    }

    @Override
    public void startGame(GameEngine gameEngine) {

    }

    public void emit (int particlesPerSecond) {
        mActivatedParticles = 0;
        mTotalMillis = 0;
        mParticlesPerMilisecond = particlesPerSecond/1000d;
        mIsEmiting = true;
    }


    public void oneShot(GameEngine gameEngine, double x, double y, int numParticles) {
        mX = x;
        mY = y;
        mIsEmiting = false;
        // We create particles based in the parameters
        for (int i=0; !mParticlePool.isEmpty() && i<numParticles; i++) {
            activateParticle(gameEngine);
        }
    }

    private void activateParticle(GameEngine gameEngine) {
        Particle p = mParticlePool.remove(0);
        for (int i=0; i<mInitializers.size(); i++) {
            mInitializers.get(i).initParticle(p, mRandom);
        }
        p.activate(gameEngine, mTimeToLive, mX, mY, mModifiers, mLayer);
        mActivatedParticles++;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (!mIsEmiting){
            return;
        }
        mTotalMillis += elapsedMillis;
        // The particles get updated on their own
        // We have to make sure that we have to keep emiting
        while ( !mParticlePool.isEmpty() && // We have particles in the pool
                mActivatedParticles < mParticlesPerMilisecond*mTotalMillis) { // and we are under the number of particles that should be launched
            // Activate a new particle
            activateParticle(gameEngine);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Nothing to do here, there is no drawing on the Particle system
    }

    public void returnToPool(Particle particle) {
        mParticlePool.add(particle);
    }

    public void setPosition(double x, double y) {
        mX = x;
        mY = y;
    }

    public void stopEmiting() {
        mIsEmiting = false;
    }

    public ParticleSystem clearInitializers() {
        mInitializers.clear();
        return this;
    }


}
