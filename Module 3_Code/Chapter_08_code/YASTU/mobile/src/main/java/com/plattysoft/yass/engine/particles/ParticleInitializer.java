package com.plattysoft.yass.engine.particles;

import java.util.Random;

/**
 * Created by Raul Portales on 02/04/15.
 */
public interface ParticleInitializer {

    void initParticle(Particle p, Random r);

}
