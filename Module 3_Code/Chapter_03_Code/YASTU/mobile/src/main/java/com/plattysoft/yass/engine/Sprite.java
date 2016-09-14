package com.plattysoft.yass.engine;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Raul Portales on 23/03/15.
 */
public abstract class Sprite extends GameObject {

    protected double mPositionX;
    protected double mPositionY;
    protected double mRotation;

    protected final double mPixelFactor;

    private final Bitmap mBitmap;
    protected final int mImageHeight;
    protected final int mImageWidth;

    private final Matrix mMatrix = new Matrix();

    protected Sprite (GameEngine gameEngine, int drawableRes) {
        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);

        mPixelFactor = gameEngine.mPixelFactor;

        mImageHeight = (int) (spriteDrawable.getIntrinsicHeight() * mPixelFactor);
        mImageWidth = (int) (spriteDrawable.getIntrinsicWidth() * mPixelFactor);

        mBitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mPositionX > canvas.getWidth()
                || mPositionY > canvas.getHeight()
                || mPositionX < -mImageWidth
                || mPositionY < -mImageHeight) {
            return;
        }
        mMatrix.reset();
        mMatrix.postScale((float) mPixelFactor, (float) mPixelFactor);
        mMatrix.postTranslate((float) mPositionX, (float) mPositionY);
        mMatrix.postRotate((float) mRotation, (float) (mPositionX + mImageWidth/2), (float) (mPositionY + mImageHeight/2));
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
}
