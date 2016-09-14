package com.packtpub.enhancedsnakegame.enhancedsnakegame;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity {

    Canvas canvas;
    SnakeAnimView snakeAnimView;

    //THe snake head sprite sheet
    Bitmap headAnimBitmap;
    Bitmap bodyBitmap;
    Bitmap tailBitmap;
    //The portion of the bitmap to be drawn in the current frame
    Rect rectToBeDrawn;
    //The dimensions of a single frame
    int frameHeight = 64;
    int frameWidth = 64;
    int numFrames = 6;
    int frameNumber;

    int screenWidth;
    int screenHeight;

    //stats
    long lastFrameTime;
    int fps;
    int hi;

    //To start the game from onTouchEvent
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        headAnimBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_sprite_sheet);
        bodyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.body);
        bodyBitmap= Bitmap.createScaledBitmap(bodyBitmap, 200, 200, false);
        tailBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail);
        tailBitmap= Bitmap.createScaledBitmap(tailBitmap, 200, 200, false);

        snakeAnimView = new SnakeAnimView(this);
        setContentView(snakeAnimView);

        i = new Intent(this, GameActivity.class);

    }

    class SnakeAnimView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingSnake;
        Paint paint;

        public SnakeAnimView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            frameWidth=headAnimBitmap.getWidth()/numFrames;
            frameHeight=headAnimBitmap.getHeight();
        }


        @Override
        public void run() {
            while (playingSnake) {
                update();
                draw();
                controlFPS();

            }

        }



        public void update() {

            //which frame should we draw
            rectToBeDrawn = new Rect((frameNumber * frameWidth), 0,
                    (frameNumber * frameWidth +frameWidth)-1, frameHeight);

            //now the next frame
            frameNumber++;

            //don't try and draw frames that don't exist
            if(frameNumber == numFrames){
                frameNumber = 0;//back to the first frame
            }
        }

        public void draw() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.argb(255,186,230,177));//the background
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(150);
                canvas.drawText("Snake", 40, 150, paint);
                paint.setTextSize(45);
                canvas.drawText("  Hi Score:" + hi, 40, screenHeight-50, paint);

                //Draw the snake head
                //make this Rect whatever size and location you like
                //(startX, startY, endX, endY)
                Rect destRect = new Rect(screenWidth/2+100, screenHeight/2-100, screenWidth/2+300, screenHeight/2+100);

                canvas.drawBitmap(headAnimBitmap, rectToBeDrawn, destRect, paint);
                canvas.drawBitmap(bodyBitmap,screenWidth/2-100,screenHeight/2-100, paint);
                canvas.drawBitmap(tailBitmap,screenWidth/2-300,screenHeight/2-100, paint);

                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        public void controlFPS() {
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 500 - timeThisFrame;
            if (timeThisFrame > 0) {
                fps = (int) (1000 / timeThisFrame);
            }
            if (timeToSleep > 0) {

                try {
                    ourThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                }

            }

            lastFrameTime = System.currentTimeMillis();
        }


        public void pause() {
            playingSnake = false;
            try {
                ourThread.join();
            } catch (InterruptedException e) {
            }

        }

        public void resume() {
            playingSnake = true;
            ourThread = new Thread(this);
            ourThread.start();
        }


        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {


            startActivity(i);
            finish();
            return true;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        while (true) {
            snakeAnimView.pause();
            break;
        }

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        snakeAnimView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeAnimView.pause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            snakeAnimView.pause();
            finish();
            return true;
        }
        return false;
    }




}
