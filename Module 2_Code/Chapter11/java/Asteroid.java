package com.gamecodeschool.c11asteroids;

import android.graphics.PointF;

import java.util.Random;

public class Asteroid extends GameObject{

    PointF[] points;

    CollisionPackage cp;


    public Asteroid(int levelNumber, int mapWidth, int mapHeight){
        super();

        // set a random rotation rate in degrees per second
        Random r = new Random();
        setRotationRate(r.nextInt(50 * levelNumber) + 10);

        // travel at any random angle
        setTravellingAngle(r.nextInt(360));

        // Spawn asteroids between 50 and 550 on x and y
        // And avoid the extreme edges of map
        int x = r.nextInt(mapWidth - 100)+50;
        int y = r.nextInt(mapHeight - 100)+50;

        // Avoid the center where the player spawns
        if(x > 250 && x < 350) x = x + 100;
        if(y > 250 && y < 350) y = y + 100;

        // Set the location
        setWorldLocation(x,y);

        // Make them a random speed with the maximum
        // being appropriate to the level number
        setSpeed(r.nextInt(25 * levelNumber)+1);

        setMaxSpeed(140);

        // Cap the speed
        if (getSpeed() > getMaxSpeed()){
            setSpeed(getMaxSpeed());
        }

        // Make sure we know this object is a ship
        setType(Type.ASTEROID);

        // Define a random asteroid shape
        // Then call the parent setVertices()
        generatePoints();

        // Initialize the collision package
        // (the object space vertex list, x any world location
        // the largest possible radius, facingAngle)
        cp = new CollisionPackage(points, getWorldLocation(), 25, getFacingAngle());

    }

    public void bounce(){

        // Reverse the travelling angle
        if(getTravellingAngle() >= 180){
            setTravellingAngle(getTravellingAngle()-180);
        }else{
            setTravellingAngle(getTravellingAngle() + 180);
        }

        // Reverse velocity because occasionally they get stuck
        setWorldLocation((getWorldLocation().x + -getxVelocity()/3), (getWorldLocation().y + -getyVelocity()/3));

        // Speed up by 10%
        setSpeed(getSpeed() * 1.1f);

        // Not too fast though
        if(getSpeed() > getMaxSpeed()){
            setSpeed(getMaxSpeed());
        }



    }

    public void update(float fps){

        setxVelocity ((float) (getSpeed() * Math.cos(Math.toRadians(getTravellingAngle() + 90))));
        setyVelocity ((float) (getSpeed() * Math.sin(Math.toRadians(getTravellingAngle() + 90))));

        move(fps);

        // Update the collision package
        cp.facingAngle = getFacingAngle();
        cp.worldLocation = getWorldLocation();

    }

    // Create a random asteroid shape
    public void generatePoints(){
        points = new PointF[7];

        Random r = new Random();
        int i;

        // First a point roughly centre below 0
        points[0] = new PointF();
        i = (r.nextInt(10))+1;
        if(i % 2 == 0){i = -i;}
        points[0].x = i;
        i = -(r.nextInt(20)+5);
        points[0].y = i;

        // Now a point still below centre but to the right and up a bit
        points[1] = new PointF();
        i = r.nextInt(14)+11;
        points[1].x = i;
        i = -(r.nextInt(12)+1);
        points[1].y =  i;

        // Above 0 to the right
        points[2] = new PointF();
        i = r.nextInt(14)+11;
        points[1].x = i;
        i = r.nextInt(12)+1;
        points[2].y = i;

        // A point roughly centre above 0
        points[3] = new PointF();
        i = (r.nextInt(10))+1;
        if(i % 2 == 0){i = -i;}
        points[3].x = i;
        i = r.nextInt(20)+5;
        points[3].y =  i;

        // left above 0
        points[4] = new PointF();
        i = -(r.nextInt(14)+11);
        points[4].x = i;
        i = r.nextInt(12)+1;
        points[4].y = i ;

        // left below 0
        points[5] = new PointF();
        i = -(r.nextInt(14)+11);
        points[5].x =  i;
        i = -(r.nextInt(12)+1);

        points[5].y = i;

        // We add on an extra point that we won't use in asteroidVertices[].
        // The point is the same as the first. This is because the last vertex
        // links back to the first to create a line. This line will need to be
        // used in calculations when we do our collision detection.

        // Here is the extra vertex- same as the first.
        points[6] = new PointF();
        points[6].x = points[0].x;
        points[6].x = points[0].x;

        // Now use these points to draw our asteroid
        float[] asteroidVertices = new float[]{
                // First point to second point
                points[0].x, points[0].y, 0,
                points[1].x, points[1].y, 0,

                // 2nd to 3rd
                points[1].x, points[1].y, 0,
                points[2].x, points[2].y, 0,

                // 3 to 4
                points[2].x, points[2].y, 0,
                points[3].x, points[3].y, 0,

                // 4 to 5
                points[3].x, points[3].y, 0,
                points[4].x, points[4].y, 0,

                // 5 to 6
                points[4].x, points[4].y, 0,
                points[5].x, points[5].y, 0,

                // 6 back to 1
                points[5].x, points[5].y, 0,
                points[0].x, points[0].y, 0,
        };

        setVertices(asteroidVertices);

    }


}
