package com.packtpub.basicclasses.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //first we make an object of type soldier
        Soldier rambo = new Soldier();
        rambo.soldierType = "Green Beret";
        rambo.health = 150;// It takes a lot to kill Rambo

        //Now we make another Soldier object
        Soldier vassily = new Soldier();
        vassily.soldierType = "Sniper";
        vassily.health = 50;//Snipers have less armour

        //And one more Soldier object
        Soldier wellington = new Soldier();
        wellington.soldierType = "Sailor";
        wellington.health = 100;//He's tough but no green beret


        Log.i("Rambo's health = ", "" + rambo.health);
        Log.i("Vassily's health = ", "" + vassily.health);
        Log.i("Wellington's health = ", "" + wellington.health);

        rambo.shootEnemy();
        vassily.shootEnemy();
        wellington.shootEnemy();


    }


  }
