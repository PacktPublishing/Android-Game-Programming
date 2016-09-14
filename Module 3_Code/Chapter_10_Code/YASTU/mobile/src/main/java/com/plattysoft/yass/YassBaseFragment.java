package com.plattysoft.yass;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.plattysoft.yass.engine.BaseCustomDialog;

/**
 * Created by Raul Portales on 06/03/15.
 */
public class YassBaseFragment extends Fragment {

    BaseCustomDialog mCurrentDialog;

    public void showDialog (BaseCustomDialog newDialog) {
        showDialog(newDialog, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getYassActivity().applyTypeface(view);
        final ViewTreeObserver obs = view.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public synchronized void onGlobalLayout() {
                ViewTreeObserver viewTreeObserver = getView().getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        viewTreeObserver.removeGlobalOnLayoutListener(this);
                    } else {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                    }
                    onLayoutCompleted();
                }
            }
        });
    }

    protected void onLayoutCompleted() {
    }

    public void showDialog (BaseCustomDialog newDialog, boolean dismissOtherDialog) {
        getYassActivity().showDialog(newDialog, dismissOtherDialog);
    }

    public YassActivity getYassActivity() {
        return (YassActivity) getActivity();
    }

    public void onSignInFailed() {
        
    }

    public void onSignInSucceeded() {

    }

    public boolean onBackPressed() {
        return false;
    }
}
