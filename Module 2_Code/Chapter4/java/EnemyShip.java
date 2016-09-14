package com.gamecodeschool.c4tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class EnemyShip{
    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;

    // Detect mines leaving the screen
    private int maxX;
    private int minX;
    // Spawn mines within screen bounds
    private int maxY;
    private int minY;

    // A hit box for collision detection
    private Rect hitBox;

    // Constructor
    public EnemyShip(Context context, int screenX, int screenY){
        Random generator = new Random();
        int whichBitmap = generator.nextInt(3);
        switch (whichBitmap){
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
                break;

            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
                break;

            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
                break;


        }

        scaleBitmap(screenX);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;


        speed = generator.nextInt(6)+10;

        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();

        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;

        //respawn when off screen
        if (x < minX - bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }

        // Refresh hit box location
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    //Getters and Setters
    public Bitmap getBitmap(){

        return bitmap;
    }
    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public Rect getHitbox(){
        return hitBox;
    }


    // This is used by the TDView update() method to
    // Make an enemy out of bounds and force a re-spawn
    public void setX(int x) {
        this.x = x;
    }

    public void scaleBitmap(int x){

        if(x < 1000) {
            bitmap = Bitmap.createScaledBitmap(bitmap,
                bitmap.getWidth() / 3,
                bitmap.getHeight() / 3,
                false);
        }else if(x < 1200){
            bitmap = Bitmap.createScaledBitmap(bitmap,
                bitmap.getWidth() / 2,
                bitmap.getHeight() / 2,
                false);
        }
    }

}
