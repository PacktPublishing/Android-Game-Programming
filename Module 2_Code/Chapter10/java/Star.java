package com.gamecodeschool.c10asteroids;

import java.util.Random;


public class Star extends GameObject{

    // Declare a random object here because
    // we will use it in the update() method
    // and we don't want GC to have to keep clearing it up
    Random r;

    public Star(int mapWidth, int mapHeight){
        setType(Type.STAR);
        r = new Random();
        setWorldLocation(r.nextInt(mapWidth),r.nextInt(mapHeight));

        // Define the star
        // as a single point
        // in exactly the coordinates as its world location
        float[] starVertices = new float[]{

                0,
                0,
                0

        };

        setVertices(starVertices);


    }

    public void update(){

        // Randomly twinkle the stars
        int n = r.nextInt(1000);
        if(n == 0){
            // Switch on or off
            if(isActive()){
                setActive(false);
            }else{
                setActive(true);
            }
        }

    }



}
