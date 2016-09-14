package com.plattysoft.yass.input;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;

import com.plattysoft.yass.YassActivity;

/**
 * Created by Raul Portales on 10/03/15.
 */
public class SensorsInputController extends InputController {

    private static final double DEGREES_PER_RADIAN = 57.2957795d;
    private static final double MAX_ANGLE = 30;

    private Activity mActivity;

    private float[] mRotationMatrix = new float[16];
    private float[] mOrientation = new float[3];
    private float[] mLastMagFields = new float[3];
    private float[] mLastAccels = new float[3];
    private int mRotation;

    private SensorEventListener mMagneticChangesListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            System.arraycopy(event.values, 0, mLastMagFields, 0, 3);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private SensorEventListener mAccelerometerChangesListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            System.arraycopy(event.values, 0, mLastAccels, 0, 3);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public SensorsInputController(YassActivity yassActivity) {
        mActivity = yassActivity;
        mRotation = yassActivity.getWindowManager().getDefaultDisplay().getRotation();
    }

    private void registerListeners() {
        SensorManager sm = (SensorManager) mActivity.getSystemService(Activity.SENSOR_SERVICE);
        sm.registerListener(mAccelerometerChangesListener,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(mMagneticChangesListener,
                sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterListeners() {
        SensorManager sm = (SensorManager) mActivity.getSystemService(Activity.SENSOR_SERVICE);
        sm.unregisterListener(mAccelerometerChangesListener);
        sm.unregisterListener(mMagneticChangesListener);
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        mHorizontalFactor = getHorizontalAxis()/45d;
//        mVerticalFactor = getVerticalAxis()/45d;
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//    }

    @Override
    public void onStart() {
        registerListeners();
    }

    @Override
    public void onStop() {
        unregisterListeners();
    }

    @Override
    public void onResume() {
        registerListeners();
    }

    @Override
    public void onPause() {
        unregisterListeners();
    }

    @Override
    public void onPreUpdate() {
        mHorizontalFactor = getHorizontalAxis()/MAX_ANGLE;
        if (mHorizontalFactor > 1) {
            mHorizontalFactor = 1;
        }
        else if (mHorizontalFactor < -1) {
            mHorizontalFactor = -1;
        }
        mVerticalFactor = 0;
    }

    private double getHorizontalAxis() {
        if (SensorManager.getRotationMatrix(mRotationMatrix, null, mLastAccels, mLastMagFields)) {
            if (mRotation == Surface.ROTATION_0) {
                SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, mRotationMatrix);
                SensorManager.getOrientation(mRotationMatrix, mOrientation);
                return mOrientation[1] * DEGREES_PER_RADIAN;
            }
            else {
                SensorManager.getOrientation(mRotationMatrix, mOrientation);
                return -mOrientation[1] * DEGREES_PER_RADIAN;
            }
        }
        else {
            // Case for devices that DO NOT have magnetic sensors
            if (mRotation == Surface.ROTATION_0) {
                return -mLastAccels[0]* 5;
            }
            else {
                return -mLastAccels[1] * -5;
            }
        }
    }
}
