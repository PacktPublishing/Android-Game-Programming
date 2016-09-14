package com.plattysoft.yass;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.plattysoft.yass.engine.BaseCustomDialog;
import com.plattysoft.yass.sound.SoundManager;

/**
 * Created by Raul Portales on 16/04/15.
 */
public class QuitDialog extends BaseCustomDialog implements View.OnClickListener {

    private QuitDialogListener mListener;
    private int mSelectedId;

    public QuitDialog(YassActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_quit);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
     }

    public void setListener(QuitDialogListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mSelectedId = v.getId();
        dismiss();
    }

    @Override
    protected void onDismissed() {
        if (mSelectedId == R.id.btn_exit) {
            mListener.exit();
        }
    }

    public interface QuitDialogListener {
        void exit();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_A ||
                event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (findViewById(R.id.btn_resume).isFocused() ||
                    findViewById(R.id.btn_exit).isFocused()) {
                // Return false, so a proper click is sent
                return false;
            }
            return true;
        }
        return false;
    }

}
