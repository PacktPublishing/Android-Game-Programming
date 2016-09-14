package com.plattysoft.yass.counter;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;

/**
 * Created by Raul Portales on 03/03/15.
 */
public class ScoreGameObject extends GameObject {

    private final TextView mText;
    private long mTotalMilis;

    public ScoreGameObject(View view, int viewResId) {
        mText = (TextView) view.findViewById(viewResId);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mTotalMilis += elapsedMillis;
    }

    @Override
    public void startGame() {
        mTotalMilis = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        mText.setText(String.valueOf(mTotalMilis));
    }
}
