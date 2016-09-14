package com.packtpub.aworkingmethod.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //all the Log.i lines print to the Android console
        Log.i("info", "I am in the onCreate method");
        //Call guess a number with three values
        //and if true is returned output - Found it!
        if(guessANumber(1,2,3)){
            Log.i("info", "Found It!");
        }else{//guessANumber returned false -didn't find it
            Log.i ("info", "Can't find it");
        }

        //continuing with the rest of the program now
        Log.i("info", "Back in onCreate");

    }

    boolean guessANumber(int try1, int try2, int try3){
        //all the Log.i lines print to the Android console
        Log.i("info", "Hi there, I am in the method body");
        //prove our parameters have arrived in the method
        //By printing them in the console
        Log.i("info", "try1 = " + try1);
        Log.i("info", "try2 = " + try2);
        Log.i("info", "try3 = " + try3);

        //we use the boolean found variable to store our true or false
        //setting it to false to begin with
        boolean found = false;

        //Create an object of the Random class so we can use it
        Random randInt = new Random();
        //Generate a random number between 0 and 5
        int randNum = randInt.nextInt(6);
        //show our random number in the console
        Log.i("info", "Our random number = " + randNum);

        //Check if any of our guesses are the same as randNum
        if(try1 == randNum || try2 == randNum || try3 == randNum){
            found = true;
            Log.i("info", "aha!");
        }else{
            Log.i("info", "hmmm");
        }

        return found;
    }


}
