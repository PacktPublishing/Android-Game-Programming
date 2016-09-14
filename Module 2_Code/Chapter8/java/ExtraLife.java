package com.gamecodeschool.c8platformgame;

public class ExtraLife extends GameObject{

    ExtraLife(float worldStartX, float worldStartY, char type) {

        final float HEIGHT = .8f;
        final float WIDTH = .65f;

        setHeight(HEIGHT); 
        setWidth(WIDTH); 

        setType(type);

        // Choose a Bitmap

        setBitmapName("life");

        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity){}
}

