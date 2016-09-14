package com.packtpub.basicclasses.app;

import android.util.Log;

/**
 * Created by John on 30/07/2014.
 */
public class Soldier {
    int health;
    String soldierType;

    void shootEnemy(){
        //lets print which type of soldier is shooting
        Log.i(soldierType, " is shooting");
    }
}
