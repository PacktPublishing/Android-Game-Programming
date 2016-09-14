package com.plattysoft.yass.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.plattysoft.yass.YassActivity;

/**
 * Created by Raul Portales on 23/03/15.
 */
public abstract class Sprite extends ScreenGameObject {

    protected final Drawable mSpriteDrawable;
    public double mRotation;

    protected final double mPixelFactor;

    protected Bitmap mBitmap;

    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint();
    public int mAlpha = 255;
    public double mScale = 1;

    protected Sprite (GameEngine gameEngine, int drawableRes, BodyType bodyType) {
        Resources r = gameEngine.getContext().getResources();
        mSpriteDrawable = r.getDrawable(drawableRes);

        mPixelFactor = gameEngine.mPixelFactor;

        mHeight = (int) (mSpriteDrawable.getIntrinsicHeight() * mPixelFactor);
        mWidth = (int) (mSpriteDrawable.getIntrinsicWidth() * mPixelFactor);

        mBitmap = obtainDefaultBitmap();

        mRadius = Math.max(mHeight, mWidth)/2;

        mBodyType = bodyType;
    }

    protected Bitmap obtainDefaultBitmap() {
        return ((BitmapDrawable) mSpriteDrawable).getBitmap();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mX > canvas.getWidth()
                || mY > canvas.getHeight()
                || mX < -mWidth
                || mY < -mHeight) {
            return;
        }
        if (YassActivity.VISUAL_COLLISION_DEBUG) {
            mPaint.setColor(Color.YELLOW);
            if (mBodyType == BodyType.Circular) {
                canvas.drawCircle((int) (mX + mWidth / 2), (int) (mY + mHeight / 2), (int) mRadius, mPaint);
            } else if (mBodyType == BodyType.Rectangular) {
                canvas.drawRect(mBoundingRect, mPaint);
            }
        }
        float scaleFactor = (float) (mPixelFactor*mScale);
        mMatrix.reset();
        mMatrix.postScale(scaleFactor, scaleFactor);
        mMatrix.postTranslate((float) mX, (float) mY);
        mMatrix.postRotate((float) mRotation, (float) (mX + mWidth*mScale / 2), (float) (mY + mHeight*mScale / 2));
        mPaint.setAlpha(mAlpha);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }
}
