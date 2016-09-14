package com.gamecodeschool.c9asteroids;

public class SpaceShip extends GameObject{

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

    }

}
