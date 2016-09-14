package com.plattysoft.yass.input;

import android.view.MotionEvent;
import android.view.View;

import com.plattysoft.yass.R;

/**
 * Created by Raul Portales on 10/03/15.
 */
public class VirtualJoystickInputController extends InputController {

    private float mStartingPositionX;
    private float mStartingPositionY;

    private final double mMaxDistance;

    public VirtualJoystickInputController(View view) {
        view.findViewById(R.id.vjoystick_main).setOnTouchListener(new VJoystickTouchListener());
        view.findViewById(R.id.vjoystick_touch).setOnTouchListener(new VFireButtonTouchListener());

        double pixelFactor = view.getHeight() / 400d;
        mMaxDistance = 50*pixelFactor;
    }

    private class VJoystickTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                mStartingPositionX = event.getX(0);
                mStartingPositionY = event.getY(0);
            }
            else if (action == MotionEvent.ACTION_UP) {
                mHorizontalFactor = 0;
                mVerticalFactor = 0;
            }
            else if (action == MotionEvent.ACTION_MOVE) {
                // Get the proportion to the max
                mHorizontalFactor = (event.getX(0) - mStartingPositionX) / mMaxDistance;
                if (mHorizontalFactor > 1) {
                    mHorizontalFactor = 1;
                }
                else if (mHorizontalFactor < -1) {
                    mHorizontalFactor = -1;
                }
                mVerticalFactor = (event.getY(0) - mStartingPositionY) / mMaxDistance;
                if (mVerticalFactor > 1) {
                    mVerticalFactor = 1;
                }
                else if (mVerticalFactor < -1) {
                    mVerticalFactor = -1;
                }
            }
            return true;
        }
    }

    private class VFireButtonTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                mIsFiring = true;
            }
            else if (action == MotionEvent.ACTION_UP) {
                mIsFiring = false;
            }
            return true;
        }
    }
}
