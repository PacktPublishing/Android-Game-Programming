package com.plattysoft.yass.tv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plattysoft.yass.R;
import com.plattysoft.yass.counter.GameFragment;

/**
 * Created by Raul Portales on 07/05/15.
 */
public class GameTvFragment extends GameFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_game_tv, container, false);
    }
}
