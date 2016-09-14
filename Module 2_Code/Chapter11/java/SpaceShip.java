package com.gamecodeschool.c11asteroids;

import android.graphics.PointF;

public class SpaceShip extends GameObject{

    boolean isThrusting;
    private boolean isPressingRight = false;
    private boolean isPressingLeft = false;

    CollisionPackage cp;

    // Next, a 2d representation using PointF of
    // the vertices. Used to build asteroidVertices
    // and to pass to the CollisionPackage constructor
    PointF[] points;

    public SpaceShip(float worldLocationX, float worldLocationY){
        super();

        // Make sure we know this object is a ship
        // So the draw() method knows what type
        // of primitive to construct from the vertices

        setType(Type.SHIP);

        setWorldLocation(worldLocationX,worldLocationY);


        float width = 15;
        float length = 20;

        setSize(width, length);

        setMaxSpeed(150);

        // It will be useful to have a copy of the
        // length and width/2 so we don't have to keep dividing by 2
        float halfW = width / 2;
        float halfL = length / 2;

        // Define the space ship shape
        // as a triangle from point to point
        // in anti clockwise order
        float [] shipVertices = new float[]{

                - halfW, - halfL, 0,
                halfW, - halfL, 0,
                0, 0 + halfL, 0

        };


        setVertices(shipVertices);


    points = new PointF[6];
    points[0] = new PointF(- halfW, - halfL);

    points[2] = new PointF(halfW, - halfL);
    points[4] = new PointF(0, 0 + halfL);

    // To make collision detection more accurate we will define some
    // more points on the midpoints of all our sides.
    // It is possible that the point of an asteroid will pass through
    // the side of the ship and we do not test for this!
    // We only test for the point of a ship passing through the side of an asteroid!!
    // This is computationally cheaper than running both tests.
    // Although not as accurate we will see it is very close.
    // We can think of this visually as adding extra sensors on the sides of our ship
    // Here we use an equation to find the midpoint of a line which you can find
    // on most good high school math web sites.
    points[1] = new PointF(points[0].x + points[2].x/2,(points[0].y + points[2].y)/2);
    points[3] = new PointF((points[2].x + points[4].x)/2,(points[2].y + points[4].y)/2);
    points[5] = new PointF((points[4].x + points[0].x)/2,(points[4].y + points[0].y)/2);

    // Initialize the collision package
    // (the object space vertex list, x any world location
    // the largest possible radius, facingAngle)

    cp = new CollisionPackage(points, getWorldLocation(), length/2, getFacingAngle());

}

    public void update(long fps){

        float speed = getSpeed();
        if(isThrusting) {
            if (speed < getMaxSpeed()){
                setSpeed(speed + 5);
            }

        }else{
            if(speed > 0) {
                setSpeed(speed - 3);
            }else {
                setSpeed(0);
            }
        }

        setxVelocity((float) (speed* Math.cos(Math.toRadians(getFacingAngle() + 90))));
        setyVelocity((float) (speed* Math.sin(Math.toRadians(getFacingAngle() + 90))));

        if(isPressingLeft){
            setRotationRate(360);
        }

        else if(isPressingRight){
            setRotationRate(-360);
        }else{
            setRotationRate(0);
        }

        move(fps);

        // Update the collision package
        cp.facingAngle = getFacingAngle();
        cp.worldLocation = getWorldLocation();
    }

    public boolean pullTrigger() {
        //Try and fire a shot
        return true;
    }


    public void setPressingRight(boolean pressingRight) {
        isPressingRight = pressingRight;
    }

    public void setPressingLeft(boolean pressingLeft) {
        isPressingLeft = pressingLeft;
    }

    public void toggleThrust() {
        isThrusting = ! isThrusting;
    }



}
