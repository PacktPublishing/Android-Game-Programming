package com.packtpub.enhancedsnakegame.enhancedsnakegame;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.games.Games;

public class MainActivity extends BaseGameActivity implements View.OnClickListener{

    SharedPreferences prefs;
    String dataName = "MyData";
    String intName = "MyInt";
    int defaultInt = 0;
    public static int hiScore;

    //Our google play buttons
    Button llPlay;
    Button awardsLink;
    com.google.android.gms.common.SignInButton sign_in_button;
    Button sign_out_button;

    Canvas canvas;
    SnakeAnimView snakeAnimView;

    //The snake head sprite sheet
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

        //load the hiscore
        prefs = getSharedPreferences(dataName,MODE_PRIVATE);
        hiScore = prefs.getInt(intName, defaultInt);

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

        //Load our UI on top of our SnakeAnimView
        LayoutInflater mInflater = LayoutInflater.from(this);
        View overView = mInflater.inflate(R.layout.activity_main, null);
        this.addContentView(overView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //game services buttons
        sign_in_button = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(this);
        sign_out_button = (Button)findViewById(R.id.sign_out_button);
        sign_out_button.setOnClickListener(this);
        awardsLink = (Button) findViewById(R.id.awardsLink);
        awardsLink.setOnClickListener(this);
        llPlay = (Button)findViewById(R.id.llPlay);
        llPlay.setOnClickListener(this);

        i = new Intent(this, GameActivity.class);

    }

    @Override
    public void onSignInFailed() {
        // Sign in has failed. So show the user the sign-in button.
        sign_in_button.setVisibility(View.VISIBLE);
        sign_out_button.setVisibility(View.GONE);
    }

    @Override
    public void onSignInSucceeded() {
        // show sign-out button, hide the sign-in button
        sign_in_button.setVisibility(View.GONE);
        sign_out_button.setVisibility(View.VISIBLE);
        llPlay.setVisibility(View.VISIBLE);
        awardsLink.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sign_in_button:
                // start the sign
                beginUserInitiatedSignIn();
                break;

            case R.id.sign_out_button:
                // sign out.
                signOut();

                // show sign-in button, hide the sign-out button
                sign_in_button.setVisibility(View.VISIBLE);
                sign_out_button.setVisibility(View.GONE);
                llPlay.setVisibility(View.GONE);
                awardsLink.setVisibility(View.GONE);
                break;

            case R.id.awardsLink:

                startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 0);

                break;

            case R.id.llPlay:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), getResources().getString(R.string.leaderboard_snake)),0);
                break;

        }
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
                canvas.drawText("  Hi Score:" + hiScore, 40, screenHeight-50, paint);

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
