package com.packtpub.enhancedsnakegame.enhancedsnakegame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends Activity {

    Canvas canvas;
    SnakeView snakeView;

    Bitmap headBitmap;
    Bitmap bodyBitmap;
    Bitmap tailBitmap;
    Bitmap appleBitmap;
    Bitmap flowerBitmap;

    //For animating the flower
    //The portion of the bitmap to be drawn in the current frame
    Rect flowerRectToBeDrawn;
    //The dimensions of a single frame
    //frame width and height need to be dynamic
    //based on block size
    int frameHeight;
    int frameWidth;
    int flowerNumFrames = 2;
    int flowerFrameNumber;
    //Measure how often the flower is animated
    int flowerAnimTimer = 0;

    //Sound
    //initialize sound variables
    private SoundPool soundPool;
    int sample1 = -1;
    int sample2 = -1;
    int sample3 = -1;
    int sample4 = -1;

    //for snake movement
    int directionOfTravel=0;
    //0 = up, 1 = right, 2 = down, 3= left


    int screenWidth;
    int screenHeight;
    int topGap;

    //stats
    long lastFrameTime;
    int fps;
    int score;
    int hi;

    //Game objects
    int [] snakeX;
    int [] snakeY;
    //for the direction each section is heading
    int [] snakeH;
    int snakeLength;
    int appleX;
    int appleY;

    //Some matrix objects to rotate our snake segments
    //Facing right is the normal orientation
    //Here are the other 3
    Matrix matrix90 = new Matrix();
    Matrix matrix180 = new Matrix();
    Matrix matrix270 = new Matrix();
    //This one reverses the head
    //a slightly different effect to the tail and body
    //because otherwise the head will be upside down
    Matrix matrixHeadFlip = new Matrix();
    //We initialize these in the configureDisplay method


    //And the pretty flowers
    int [] flowersX;
    int [] flowersY;

    //The size in pixels of a place on the game board
    int blockSize;
    int numBlocksWide;
    int numBlocksHigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadSound();
        configureDisplay();
        snakeView = new SnakeView(this);
        setContentView(snakeView);

    }

    class SnakeView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingSnake;
        Paint paint;

        public SnakeView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();

            //Even my 9 year old play tester couldn't
            //get a snake this long
            snakeX = new int[200];
            snakeY = new int[200];
            snakeH = new int[200];

            //arrange some flowers
            plantFlowers();
            //our starting snake
            getSnake();
            //get an apple to munch
            getApple();
        }

        public void plantFlowers(){
            Random random = new Random();
            int x = 0;
            int y = 0;
            flowersX = new int[200];
            flowersY = new int[200];

            for(int i = 0;i < 10; i++){
                x = random.nextInt(numBlocksWide-1)+1;
                y = random.nextInt(numBlocksHigh-1)+1;
                flowersX[i] = x;
                flowersY[i] = y;
            }

        }

        public void getSnake(){
            snakeLength = 3;
            //start snake head in the middle of screen
            snakeX[0] = numBlocksWide/2;
            snakeY[0] = numBlocksHigh /2;

            //Then the body
            snakeX[1] = snakeX[0]-1;
            snakeY[1] = snakeY[0];

            //And the tail
            snakeX[1] = snakeX[1]-1;
            snakeY[1] = snakeY[0];
        }

        public void getApple(){
            Random random = new Random();
            appleX = random.nextInt(numBlocksWide-1)+1;
            appleY = random.nextInt(numBlocksHigh-1)+1;
        }

        @Override
        public void run() {
            while (playingSnake) {
                updateGame();
                drawGame();
                controlFPS();


            }

        }

        public void updateGame() {

            //Did the player get the apple
            if(snakeX[0] == appleX && snakeY[0] == appleY){
                //grow the snake
                snakeLength++;
                //replace the apple
                getApple();
                //add to the score
                score = score + snakeLength;
                soundPool.play(sample1, 1, 1, 0, 0, 1);
            }

            //move the body - starting at the back
            for(int i=snakeLength; i >0 ; i--){
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];

                //change heading
                snakeH[i] = snakeH[i-1];
            }

            //Move the head in the appropriate direction
            switch (directionOfTravel){
                case 0://up
                    snakeY[0]  --;
                    snakeH[0] = 0;
                    break;

                case 1://right
                    snakeX[0] ++;
                    snakeH[0] = 1;
                    break;

                case 2://down
                    snakeY[0] ++;
                    snakeH[0] = 2;
                    break;

                case 3://left
                    snakeX[0] --;
                    snakeH[0] = 3;
                    break;
            }

            //Have we had an accident
            boolean dead = false;
            //with a wall
            if(snakeX[0] == -1)dead=true;
            if(snakeX[0] >= numBlocksWide)dead=true;
            if(snakeY[0] == -1)dead=true;
            if(snakeY[0] == numBlocksHigh)dead=true;
            //or eaten ourselves?
            for (int i = snakeLength-1; i > 0; i--) {
                if ((i > 4) && (snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) {
                    dead = true;
                }
            }


            if(dead){
                //start again
                soundPool.play(sample4, 1, 1, 0, 0, 1);
                score = 0;
                getSnake();

            }

        }

        public void drawGame() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.argb(255,186,230,177));//the background
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(topGap/2);
                canvas.drawText("Score:" + score + "  Hi:" + hi, 10, topGap-6, paint);

                //draw a border - 4 lines, top right, bottom , left
                paint.setStrokeWidth(3);//4 pixel border
                canvas.drawLine(1,topGap,screenWidth-1,topGap,paint);
                canvas.drawLine(screenWidth-1,topGap,screenWidth-1,topGap+(numBlocksHigh*blockSize),paint);
                canvas.drawLine(screenWidth-1,topGap+(numBlocksHigh*blockSize),1,topGap+(numBlocksHigh*blockSize),paint);
                canvas.drawLine(1,topGap, 1,topGap+(numBlocksHigh*blockSize), paint);

                //Draw our flowers
                Rect destRect;
                Bitmap rotatedBitmap;
                Bitmap rotatedTailBitmap;

                for (int i = 0;i < 10;i++){
                    destRect = new Rect(flowersX[i]*blockSize, (flowersY[i]*blockSize)+topGap, (flowersX[i]*blockSize)+blockSize, (flowersY[i]*blockSize)+topGap+blockSize);
                    canvas.drawBitmap(flowerBitmap, flowerRectToBeDrawn, destRect, paint);
                    //canvas.drawBitmap(flowerBitmap, flowersX[i]*blockSize, (flowersY[i]*blockSize)+topGap, paint);

                }
                //Draw the snake
                rotatedBitmap = headBitmap;
                switch (snakeH[0]){
                    case 0://up
                        rotatedBitmap = Bitmap.createBitmap(rotatedBitmap , 0, 0, rotatedBitmap .getWidth(), rotatedBitmap .getHeight(), matrix270, true);
                        break;
                    case 1://right
                        //no rotation necessary

                        break;
                    case 2://down
                        rotatedBitmap = Bitmap.createBitmap(rotatedBitmap , 0, 0, rotatedBitmap .getWidth(), rotatedBitmap .getHeight(), matrix90, true);
                        break;

                    case 3://left
                        rotatedBitmap = Bitmap.createBitmap(rotatedBitmap , 0, 0, rotatedBitmap .getWidth(), rotatedBitmap .getHeight(), matrixHeadFlip, true);
                        break;


                }
                canvas.drawBitmap(rotatedBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                //Draw the body

                rotatedBitmap = bodyBitmap;
                for(int i = 1; i < snakeLength-1;i++){

                    switch (snakeH[i]){
                        case 0://up
                            rotatedBitmap = Bitmap.createBitmap(bodyBitmap , 0, 0, bodyBitmap .getWidth(), bodyBitmap .getHeight(), matrix270, true);
                            break;
                        case 1://right
                            //no rotation necessary

                            break;
                        case 2://down
                            rotatedBitmap = Bitmap.createBitmap(bodyBitmap , 0, 0, bodyBitmap .getWidth(), bodyBitmap .getHeight(), matrix90, true);
                            break;

                        case 3://left
                            rotatedBitmap = Bitmap.createBitmap(bodyBitmap , 0, 0, bodyBitmap .getWidth(), bodyBitmap .getHeight(), matrix180, true);
                            break;


                    }

                    canvas.drawBitmap(rotatedBitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                }


                //draw the tail
                //make rotated bitmap hold just the current frame of the tail
                //Otherwise we will get strange effects when rotating
                rotatedTailBitmap = Bitmap.createBitmap(tailBitmap, flowerRectToBeDrawn.left, flowerRectToBeDrawn.top, flowerRectToBeDrawn.right - flowerRectToBeDrawn.left, flowerRectToBeDrawn.bottom);

                switch (snakeH[snakeLength-1]){
                    case 0://up
                        rotatedTailBitmap = Bitmap.createBitmap(rotatedTailBitmap , 0, 0, rotatedTailBitmap .getWidth(), rotatedTailBitmap .getHeight(), matrix270, true);
                        break;
                    case 1://right
                        //no rotation necessary

                        break;
                    case 2://down
                        rotatedTailBitmap = Bitmap.createBitmap(rotatedTailBitmap , 0, 0, rotatedTailBitmap .getWidth(), rotatedTailBitmap .getHeight(), matrix90, true);
                        break;

                    case 3://left
                        rotatedTailBitmap = Bitmap.createBitmap(rotatedTailBitmap , 0, 0, rotatedTailBitmap .getWidth(), rotatedTailBitmap .getHeight(), matrix180, true);
                        break;


                }

                canvas.drawBitmap(rotatedTailBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);


                //draw the apple
                canvas.drawBitmap(appleBitmap, appleX*blockSize, (appleY*blockSize)+topGap, paint);

                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        public void controlFPS() {
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 200 - timeThisFrame;
            if (timeThisFrame > 0) {
                fps = (int) (1000 / timeThisFrame);
            }
            if (timeToSleep > 0) {

                try {
                    ourThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                }

            }

            //control the flower animation
            flowerAnimTimer++;
            //change the frame every 6 game frames
            if(flowerAnimTimer == 6){
                //which frame should we draw
                if(flowerFrameNumber == 1){
                    flowerFrameNumber = 0;
                }else{
                    flowerFrameNumber =1;
                }

                flowerRectToBeDrawn = new Rect((flowerFrameNumber * frameWidth), 0,
                (flowerFrameNumber * frameWidth +frameWidth)-1, frameHeight);

                flowerAnimTimer = 0;
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

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    if (motionEvent.getX() >= screenWidth / 2) {
                        //turn right
                        directionOfTravel ++;
                        if(directionOfTravel == 4) {//no such direction
                            //loop back to 0(up)
                            directionOfTravel = 0;
                        }
                    } else {
                        //turn left
                        directionOfTravel--;
                        if(directionOfTravel == -1) {//no such direction
                            //loop back to 0(up)
                            directionOfTravel = 3;
                        }
                    }
            }
            return true;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        while (true) {
            snakeView.pause();
            break;
        }

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        snakeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeView.pause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            snakeView.pause();



            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return false;
    }

    public void loadSound(){
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            //Create objects of the 2 required classes
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;

            //create our three fx in memory ready for use
            descriptor = assetManager.openFd("sample1.ogg");
            sample1 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample4.ogg");
            sample4 = soundPool.load(descriptor, 0);


        } catch (IOException e) {
            //catch exceptions here
        }
    }

    public void configureDisplay(){
        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        topGap = screenHeight/14;

        //Determine the size of each block/place on the game board
        blockSize = screenWidth/30;

        //Determine how many game blocks will fit into the height and width
        //Leave one block for the score at the top
        numBlocksWide = 30;
        numBlocksHigh = ((screenHeight - topGap ))/blockSize;

        //Load and scale bitmaps
        headBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head);
        bodyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.body);
        //tailBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail);
        appleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.apple);

        //scale the bitmaps to match the block size
        headBitmap = Bitmap.createScaledBitmap(headBitmap, blockSize, blockSize, false);
        bodyBitmap = Bitmap.createScaledBitmap(bodyBitmap, blockSize, blockSize, false);
        //tailBitmap = Bitmap.createScaledBitmap(tailBitmap, blockSize, blockSize, false);
        appleBitmap = Bitmap.createScaledBitmap(appleBitmap, blockSize, blockSize, false);

        //for the tail
        tailBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail_sprite_sheet);
        tailBitmap = Bitmap.createScaledBitmap(tailBitmap, blockSize*flowerNumFrames, blockSize, false);

        //for the flower
        flowerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flower_sprite_sheet);
        flowerBitmap = Bitmap.createScaledBitmap(flowerBitmap, blockSize*flowerNumFrames, blockSize, false);

        //These two lines work for the flower and the tail
        frameWidth=flowerBitmap.getWidth()/flowerNumFrames;
        frameHeight=flowerBitmap.getHeight();

        //Initialize matrix objects ready for us in drawGame
        matrix90.postRotate(90);
        matrix180.postRotate(180);
        matrix270.postRotate(270);
        //And now the head flipper
        matrixHeadFlip.setScale(-1,1);
        matrixHeadFlip.postTranslate(headBitmap.getWidth(),0);

        //setup the first frame of the flower drawing
        flowerRectToBeDrawn = new Rect((flowerFrameNumber * frameWidth), 0,
                (flowerFrameNumber * frameWidth +frameWidth)-1, frameHeight);


    }


}
