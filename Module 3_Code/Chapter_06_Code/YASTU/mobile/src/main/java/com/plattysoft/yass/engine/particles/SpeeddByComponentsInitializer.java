package com.plattysoft.yass.engine.particles;

import java.util.Random;

/**
 * Created by Raul Portales on 02/04/15.
 */
public class SpeeddByComponentsInitializer implements ParticleInitializer {

    private double mMinSpeedX;
    private double mMaxSpeedX;
    private double mMinSpeedY;
    private double mMaxSpeedY;

    public SpeeddByComponentsInitializer(double speedMinX, double speedMaxX, double speedMinY, double speedMaxY) {
        mMinSpeedX = speedMinX;
        mMaxSpeedX = speedMaxX;
        mMinSpeedY = speedMinY;
        mMaxSpeedY = speedMaxY;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        p.mSpeedX = (r.nextDouble()*(mMaxSpeedX-mMinSpeedX)+mMinSpeedX)/1000d;
        p.mSpeedY = (r.nextDouble()*(mMaxSpeedY-mMinSpeedY)+mMinSpeedY)/1000d;
    }
}
