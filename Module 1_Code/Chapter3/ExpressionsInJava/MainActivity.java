package com.packtpub.expressionsinjava.expressionsinjava;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {
    boolean c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //first we declare and initialize few variables
        int a = 10;
        String b = "Alan Turing";
        boolean c = true;

        //Let's look at how Android 'sees' these variables
        //by outputting them, one at a time to the console
        Log.i("info", "a = " + a);
        Log.i("info", "b = " + b);
        Log.i("info", "c = " + c);

        //Now let's make some changes and output them again
        a++;
        a = a+ 10;
        b = b + " was smarter than the average bear Booboo";
        b = b + a;
        c = (1+1==3);//1+1 is definitely 2! So false.

        //Now to output them all again
        Log.i("info", "a = " + a);
        Log.i("info", "b = " + b);
        Log.i("info", "c = " + c);
    }

}
