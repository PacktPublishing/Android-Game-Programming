package com.plattysoft.yass.counter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plattysoft.yass.YassActivity;
import com.plattysoft.yass.R;
import com.plattysoft.yass.YassBaseFragment;

/**
 * Created by Raul Portales on 03/03/15.
 */
public class MainMenuFragment extends YassBaseFragment implements View.OnClickListener {

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((YassActivity)getActivity()).startGame();
    }
}
