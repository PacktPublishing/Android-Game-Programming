package com.plattysoft.yass.engine.particles;

/**
 * Created by Raul Portales on 03/04/15.
 */
public class AlphaModifier implements ParticleModifier {

    private int mInitialValue;
    private int mFinalValue;
    private long mStartTime;
    private long mEndTime;
    private float mDuration;
    private float mValueIncrement;

    public AlphaModifier(int initialValue, int finalValue, long startMilis, long endMilis) {
        mInitialValue = initialValue;
        mFinalValue = finalValue;
        mStartTime = startMilis;
        mEndTime = endMilis;
        mDuration = mEndTime - mStartTime;
        mValueIncrement = mFinalValue-mInitialValue;
    }

    @Override
    public void apply(Particle particle, long miliseconds) {
        if (miliseconds < mStartTime) {
            particle.mAlpha = mInitialValue;
        }
        else if (miliseconds > mEndTime) {
            particle.mAlpha = mFinalValue;
        }
        else {
            double percentageValue = (miliseconds- mStartTime)*1d/mDuration;
            int newAlphaValue = (int) (mInitialValue + mValueIncrement*percentageValue);
            particle.mAlpha = newAlphaValue;
        }
    }

}
