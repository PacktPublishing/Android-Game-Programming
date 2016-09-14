package com.plattysoft.yass.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by Raul Portales on 26/03/15.
 */
public class FPSCounter extends GameObject {
    private final float mTextWidth;
    private final float mTextHeight;

    private Paint mPaint;
    private long mTotalMillis;
    private int mDraws;
    private float mFps;

    private String mFpsText = "";

    public FPSCounter(GameEngine gameEngine) {
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mTextHeight = (float) (25*gameEngine.mPixelFactor);
        mTextWidth = (float) (50*gameEngine.mPixelFactor);
        mPaint.setTextSize(mTextHeight/2);
    }

    @Override
    public void startGame() {
        mTotalMillis = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mTotalMillis += elapsedMillis;
        if (mTotalMillis > 1000) {
            mFps = mDraws*1000 / mTotalMillis;
            mFpsText = mFps+" fps";
            mTotalMillis = 0;
            mDraws = 0;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(0,(int)(canvas.getHeight()-mTextHeight), mTextWidth, canvas.getHeight(), mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText(mFpsText, mTextWidth/2, (int) (canvas.getHeight()-mTextHeight/2), mPaint);
        mDraws++;
    }
}
