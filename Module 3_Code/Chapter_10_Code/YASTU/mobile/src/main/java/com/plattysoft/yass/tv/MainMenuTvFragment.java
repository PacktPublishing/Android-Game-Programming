package com.plattysoft.yass.tv;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.plattysoft.yass.R;
import com.plattysoft.yass.counter.MainMenuFragment;

/**
 * Created by Raul Portales on 08/05/15.
 */
public class MainMenuTvFragment extends MainMenuFragment {
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_menu_tv;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getView().findViewById(R.id.main_menu_bg).setVisibility(View.GONE);
//    }
}
