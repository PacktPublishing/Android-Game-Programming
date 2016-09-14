package com.plattysoft.yass.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Portales on 03/04/15.
 */
public class Collision {

    private static List<Collision> sCollisionPool = new ArrayList<Collision>();

    public static Collision init(ScreenGameObject objectA, ScreenGameObject objectB) {
        if (sCollisionPool.isEmpty()) {
            return new Collision(objectA, objectB);
        }
        return sCollisionPool.remove(0);
    }

    public static void release(Collision c) {
        sCollisionPool.add(c);
    }

    public ScreenGameObject mObjectA;
    public ScreenGameObject mObjectB;

    public Collision(ScreenGameObject objectA, ScreenGameObject objectB) {
        mObjectA = objectA;
        mObjectB = objectB;
    }

    public boolean equals (Collision c) {
        return (mObjectA == c.mObjectA && mObjectB == c.mObjectB)
                || (mObjectA == c.mObjectB && mObjectB == c.mObjectA);
    }
}
