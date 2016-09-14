package com.plattysoft.yass.engine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.plattysoft.yass.R;
import com.plattysoft.yass.YassActivity;

import static android.view.View.*;

/**
 * Created by Raul Portales on 15/04/15.
 */
public class BaseCustomDialog implements OnTouchListener { //, Animation.AnimationListener {

    private boolean mIsShowing;

    protected final YassActivity mParent;
    private ViewGroup mRootLayout;
    private View mRootView;

    public BaseCustomDialog(YassActivity activity) {
        mParent = activity;
    }

    protected void onViewClicked() {
        // Ignore clicks on this view
    }

    protected void setContentView(int dialogResId) {
        ViewGroup activityRoot = (ViewGroup) mParent.findViewById(android.R.id.content);
        mRootView = LayoutInflater.from(mParent).inflate(dialogResId, activityRoot, false);
        mParent.applyTypeface(mRootView);
    }

    public void show() {
        if (mIsShowing) {
            return;
        }
        mIsShowing = true;

        ViewGroup activityRoot = (ViewGroup) mParent.findViewById(android.R.id.content);
        mRootLayout = (ViewGroup) LayoutInflater.from(mParent).inflate(R.layout.my_overlay_dialog, activityRoot, false);
        activityRoot.addView(mRootLayout);
        mRootLayout.setOnTouchListener(this);
        mRootLayout.addView(mRootView);
        // Use a fade in animation
//        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.animator.fade_in);
//        Animation dialogIn = AnimationUtils.loadAnimation(getContext(), R.animator.dialog_in);
//        mRootLayout.startAnimation(fadeIn);
//        mRootView.startAnimation(dialogIn);
//        mRootLayout.setVisibility(VISIBLE);
//        if (listener != null) {
//            listener.setOnBackPressHandler(this);
//        }
    }

    public void dismiss() {
        if (!mIsShowing) {
            return;
        }
        mIsShowing = false;
        hideViews();
//        Animation dialogOut = AnimationUtils.loadAnimation(getContext(), R.animator.dialog_out);
//        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.animator.fade_out);
//        fadeOut.setAnimationListener(this);
//        mRootView.startAnimation(dialogOut);
//        mRootLayout.startAnimation(fadeOut);
    }

    private void hideViews() {
        mRootLayout.removeView(mRootView);
//        mRootLayout.setVisibility(GONE);
        ViewGroup activityRoot = (ViewGroup) mParent.findViewById(android.R.id.content);
        activityRoot.removeView(mRootLayout);
//        if (mListener != null) {
//            mListener.onDismissed();
////            mListener.setOnBackPressHandler(null);
//            mListener = null;
//        }
    }

    protected View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Ignoring touch events on the gray outside
        return true;
    }

    public boolean isShowing() {
        return mIsShowing;
    }

//    @Override
//    public boolean onBackPressed() {
//        dismiss();
//        return true;
//    }

//    @Override
//    public void onAnimationStart(Animation paramAnimation) {
//    }
//
//    @Override
//    public void onAnimationEnd(Animation paramAnimation) {
//        mRootLayout.removeView(mRootView);
//        mRootLayout.setVisibility(GONE);
//        if (mListener != null) {
//            mListener.onDismissed();
//            mListener.setOnBackPressHandler(null);
//            mListener = null;
//        }
//    }
//
//    @Override
//    public void onAnimationRepeat(Animation paramAnimation) {
//    }
}
