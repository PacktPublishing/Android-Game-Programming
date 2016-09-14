package com.gamecodeschool.c5platformgame;

import android.content.Context;

public class Player extends GameObject {


    Player(Context context, float worldStartX, float worldStartY, int pixelsPerMetre) {
        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT); // 2 metre tall
        setWidth(WIDTH); // 1 metre wide

        setType('p');

        // Choose a Bitmap
        // This is a prite sheet with multiple frames
        // of animation. SO it will look silly until we animate it
        // In chapter 6.
        setBitmapName("player");

        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
    }

    public void update(long fps, float gravity) {
    }

}