package com.packtpub.selftestc3q3.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int x = 10;
        int y = 9;
        boolean isTrueOrFalse = false;
        isTrueOrFalse = (((x <=y)||(x == 10))&&((!isTrueOrFalse) || (isTrueOrFalse)));
        Log.i("isTrueOrFalse = ", "" + isTrueOrFalse);


    }


}
