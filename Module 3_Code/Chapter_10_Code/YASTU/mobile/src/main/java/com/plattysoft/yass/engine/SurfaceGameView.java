package com.plattysoft.yass.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.List;

/**
 * Created by Raul Portales on 23/03/15.
 */
public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {

    private List<List<GameObject>> mLayers;
    private boolean mReady;

    public SurfaceGameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public SurfaceGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public SurfaceGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void setGameObjects(List<List<GameObject>> gameObjects) {
        mLayers = gameObjects;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mReady = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mReady = false;
    }

    @Override
    public void draw() {
        if (!mReady) {
            return;
        }
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawRGB(0,0,0);
        synchronized (mLayers) {
            int numLayers = mLayers.size();
            for (int i = 0; i < numLayers; i++) {
                List<GameObject> currentLayer = mLayers.get(i);
                int numObjects = currentLayer.size();
                for (int j=0; j<numObjects; j++) {
                    currentLayer.get(j).onDraw(canvas);
                }
            }
        }
        getHolder().unlockCanvasAndPost(canvas);
    }
}
