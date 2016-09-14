package com.gamecodeschool.c8platformgame;

import android.graphics.PointF;

public class Drone extends GameObject {

    long lastWaypointSetTime;
    PointF currentWaypoint;

    final float MAX_X_VELOCITY = 3;
    final float MAX_Y_VELOCITY = 3;
    private PointF waypoint;


    Drone(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT); // 1 metre tall
        setWidth(WIDTH); // 1 metres wide

        setType(type);

        setBitmapName("drone");

        setMoves(true);
        setActive(true);
        setVisible(true);



        currentWaypoint = new PointF();

        // Where does the drone start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
        setFacing(RIGHT);
    }


    public void update(long fps, float gravity) {
        if (currentWaypoint.x > getWorldLocation().x) {
            setxVelocity(MAX_X_VELOCITY);
        } else if (currentWaypoint.x < getWorldLocation().x) {
            setxVelocity(-MAX_X_VELOCITY);
        } else {
            setxVelocity(0);
        }


        if (currentWaypoint.y >= getWorldLocation().y) {
            setyVelocity(MAX_Y_VELOCITY);
        } else if (currentWaypoint.y < getWorldLocation().y) {
            setyVelocity(-MAX_Y_VELOCITY);
        } else {
            setyVelocity(0);
        }


        // update the drone hitbox
        setRectHitbox();


        move(fps);


    }

    public void setWaypoint(Vector2Point5D playerLocation) {
        if (System.currentTimeMillis() > lastWaypointSetTime + 2000) {//Has 2 seconds passed
            lastWaypointSetTime = System.currentTimeMillis();
            currentWaypoint.x = playerLocation.x;
            currentWaypoint.y = playerLocation.y;
        }

    }
}
