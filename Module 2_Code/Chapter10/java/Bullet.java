package com.gamecodeschool.c10asteroids;

import android.graphics.PointF;

public class Bullet extends GameObject {

    private boolean inFlight = false;



    public Bullet(float shipX, float shipY) {
        super();

        setType(Type.BULLET);

        setWorldLocation(shipX, shipY);

        // Define the bullet
        // as a single point
        // in exactly the coordinates as its world location
        float[] bulletVertices = new float[]{

                0,
                0,
                0

        };

        setVertices(bulletVertices);

        

    }

    public void shoot(float shipFacingAngle){
        setFacingAngle(shipFacingAngle);
        inFlight = true;
        setSpeed (300);
    }

    public void resetBullet(PointF shipLocation){
        // Remove the velocity if bullet out of bounds
        inFlight = false;
        setxVelocity(0);
        setyVelocity(0);
        setSpeed(0);
        setWorldLocation(shipLocation.x, shipLocation.y);

    }

    public boolean isInFlight(){
        return  inFlight;
    }

    public void update(long fps, PointF shipLocation){
        // Set the velocity if bullet in flight
        if(inFlight){
            setxVelocity((float)(getSpeed()* Math.cos(Math.toRadians(getFacingAngle() + 90))));
            setyVelocity((float)(getSpeed()* Math.sin(Math.toRadians(getFacingAngle() + 90))));
        }else{
            // Have it sit inside the ship
            setWorldLocation(shipLocation.x, shipLocation.y);
        }



        move(fps);
    }
}
