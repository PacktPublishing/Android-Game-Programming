package com.plattysoft.yass;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.plattysoft.yass.engine.BaseCustomDialog;

/**
 * Created by Raul Portales on 06/03/15.
 */
public class YassBaseFragment extends Fragment {

    BaseCustomDialog mCurrentDialog;

    public void showDialog (BaseCustomDialog newDialog) {
        showDialog(newDialog, false);
    }

    public void showDialog (BaseCustomDialog newDialog, boolean dismissOtherDialog) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (dismissOtherDialog) {
                mCurrentDialog.dismiss();
            }
            else {
                return;
            }
        }
        mCurrentDialog = newDialog;
        mCurrentDialog.show();
    }

    public boolean onBackPressed() {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            mCurrentDialog.dismiss();
            return true;
        }
        return false;
    }

    protected YassActivity getYassActivity() {
        return (YassActivity) getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getYassActivity().applyTypeface(view);
    }
}
