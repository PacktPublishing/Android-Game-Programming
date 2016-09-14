package com.plattysoft.yass;

import android.app.Fragment;

import com.plattysoft.yass.tv.GameTvFragment;
import com.plattysoft.yass.tv.MainMenuTvFragment;

/**
 * Created by Raul Portales on 07/05/15.
 */
public class YassTvActivity extends YassActivity {

    public void startGame() {
        // Navigate the the game fragment, which makes the start automatically
        navigateToFragment( new GameTvFragment());
    }

    protected Fragment createMenuFragment() {
        return new MainMenuTvFragment();
    }
}
