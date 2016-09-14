package com.plattysoft.yass.engine.particles;

import java.util.Random;

/**
 * Created by Raul Portales on 03/04/15.
 */
public class RotationSpeedInitializer implements ParticleInitializer {

    private double mMinRotationSpeed;
    private double mMaxRotationSpeed;

    public RotationSpeedInitializer(double minRotationSpeed,	double maxRotationSpeed) {
        mMinRotationSpeed = minRotationSpeed;
        mMaxRotationSpeed = maxRotationSpeed;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        double rotationSpeed = r.nextDouble()*(mMaxRotationSpeed-mMinRotationSpeed) + mMinRotationSpeed;
        p.mRotationSpeed = rotationSpeed;
    }

}
