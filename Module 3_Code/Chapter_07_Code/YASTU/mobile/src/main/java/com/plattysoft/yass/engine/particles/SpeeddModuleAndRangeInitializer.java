package com.plattysoft.yass.engine.particles;

import java.util.Random;

/**
 * Created by Raul Portales on 02/04/15.
 */
public class SpeeddModuleAndRangeInitializer implements ParticleInitializer {

    private double mSpeedMin;
    private double mSpeedMax;
    private int mMinAngle;
    private int mMaxAngle;

    public SpeeddModuleAndRangeInitializer(double speedMin, double speedMax, int minAngle, int maxAngle) {
        mSpeedMin = speedMin;
        mSpeedMax = speedMax;
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        double speed = r.nextDouble()*(mSpeedMax-mSpeedMin) + mSpeedMin;
        int angle;
        if (mMaxAngle == mMinAngle) {
            angle = mMinAngle;
        }
        else {
            angle = r.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
        }
        double angleInRads = angle*Math.PI/180f;
        p.mSpeedX = speed * Math.cos(angleInRads)/1000d;
        p.mSpeedY = speed * Math.sin(angleInRads)/1000d;
    }
}
