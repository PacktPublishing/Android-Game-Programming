package com.plattysoft.yass.engine.particles;

import java.util.Random;

/**
 * Created by Raul Portales on 03/04/15.
 */
public class RotationInitiazer implements ParticleInitializer {

    private int mMinAngle;
    private int mMaxAngle;

    public RotationInitiazer(int minAngle, int maxAngle) {
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        int value = r.nextInt(mMaxAngle-mMinAngle)+mMinAngle;
        p.mRotation = value;
    }

}
