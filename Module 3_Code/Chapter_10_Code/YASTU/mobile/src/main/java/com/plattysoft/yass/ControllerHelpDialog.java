package com.plattysoft.yass;

import android.view.KeyEvent;
import android.view.View;

import com.plattysoft.yass.engine.BaseCustomDialog;

/**
 * Created by Raul Portales on 12/05/15.
 */
public class ControllerHelpDialog extends BaseCustomDialog implements View.OnClickListener {
    public ControllerHelpDialog(YassActivity a) {
        super(a);
        setContentView(R.layout.dialog_controller_help);
        findViewById(R.id.controller_help_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_A ||
                event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            dismiss();
            return true;
        }
        return false;
    }
}
