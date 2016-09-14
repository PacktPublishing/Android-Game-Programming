package com.gamecodeschool.c11asteroids;

public class Border extends GameObject{


    public Border(float mapWidth, float mapHeight){

        setType(Type.BORDER);
        //border centre is the exact centre of map
        setWorldLocation(mapWidth/2,mapHeight/2);

        float w = mapWidth;
        float h = mapHeight;
        setSize(w, h);

        // The vertices of the border represent four lines
        float[] borderVertices = new float[]{
                // A line from point 1 to point 2
                - w/2, -h/2, 0,
                w/2, -h/2, 0,
                // Point 2 to point 3
                w/2, -h/2, 0,
                w/2, h/2, 0,
                // Point 3 to point 4
                w/2, h/2, 0,
                -w/2, h/2, 0,
                // Point 4 to point 1
                -w/2, h/2, 0,
                - w/2, -h/2, 0,
        };

        setVertices(borderVertices);

    }

}
