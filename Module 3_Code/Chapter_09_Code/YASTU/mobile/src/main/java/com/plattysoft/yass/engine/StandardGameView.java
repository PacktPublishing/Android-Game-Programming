package com.plattysoft.yass.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Raul Portales on 23/03/15.
 */
public class StandardGameView extends View implements GameView {

    private List<List<GameObject>> mLayers;

    public StandardGameView(Context context) {
        super(context);
    }

    public StandardGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StandardGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw() {
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
    }

    public void setGameObjects(List<List<GameObject>> gameObjects) {
        mLayers = gameObjects;
    }
}
