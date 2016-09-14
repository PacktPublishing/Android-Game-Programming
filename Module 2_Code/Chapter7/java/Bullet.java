package com.gamecodeschool.c7platformgame;

public class Bullet  {

    private float x;
    private float y;
    private float xVelocity;
    private int direction;

    Bullet(float x, float y, int speed, int direction){
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.xVelocity = speed * direction;
    }

    public int getDirection(){
        return direction;
    }

    public void update(long fps, float gravity){
        x += xVelocity / fps;
    }

    public void hideBullet(){
        this.x = -100;
        this.xVelocity = 0;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

}

