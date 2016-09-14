package com.plattysoft.yass.engine;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Raul Portales on 24/04/15.
 */
public abstract class AnimatedSprite extends Sprite {

    private final AnimationDrawable mAnimationDrawable;
    private int mTotalTime;
    private long mCurrentTime;

    public AnimatedSprite(GameEngine gameEngine, int drawableRes, BodyType bodyType) {
        super(gameEngine, drawableRes, bodyType);
        // Now, the drawable must be an animation drawable
        mAnimationDrawable = (AnimationDrawable) mSpriteDrawable;
        // Calculate the total time of the animation
        mTotalTime = 0;
        for (int i=0; i<mAnimationDrawable.getNumberOfFrames(); i++) {
            mTotalTime += mAnimationDrawable.getDuration(i);
        }
    }

    @Override
    protected Bitmap obtainDefaultBitmap() {
        AnimationDrawable ad = (AnimationDrawable) mSpriteDrawable;
        return ((BitmapDrawable) ad.getFrame(0)).getBitmap();
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mCurrentTime += elapsedMillis;
        if (mCurrentTime > mTotalTime) {
            if (mAnimationDrawable.isOneShot()) {
                return;
            }
            else {
                mCurrentTime = mCurrentTime % mTotalTime;
            }
        }
        long animationElapsedTime = 0;
        for (int i=0; i<mAnimationDrawable.getNumberOfFrames(); i++) {
            animationElapsedTime += mAnimationDrawable.getDuration(i);
            if (animationElapsedTime > mCurrentTime) {
                mBitmap = ((BitmapDrawable) mAnimationDrawable.getFrame(i)).getBitmap();
                break;
            }
        }
    }
}
