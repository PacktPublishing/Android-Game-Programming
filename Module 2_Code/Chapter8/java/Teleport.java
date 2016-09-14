package com.gamecodeschool.c8platformgame;


public class Teleport extends GameObject {

    Location target;

    Teleport(float worldStartX, float worldStartY, char type, Location target) {


        final float HEIGHT = 2;
        final float WIDTH = 2;

        setHeight(HEIGHT); // 2 metres tall

        setWidth(WIDTH); // 1 metre wide

        setType(type);

        // Choose a Bitmap

        setBitmapName("door");

        this.target = new Location(target.level, target.x, target.y);

        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);

        setRectHitbox();
    }

    public Location getTarget(){
        return target;
    }

    public void update(long fps, float gravity){}
}
