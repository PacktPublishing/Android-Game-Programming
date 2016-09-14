package com.gamecodeschool.c7platformgame;

import android.graphics.PointF;

public class PlayerState {

    private int numCredits;
    private int mgFireRate;
    private int lives;
    private float restartX;
    private float restartY;

    PlayerState() {
        lives = 3;
        mgFireRate = 1;
        numCredits = 0;
    }

    public void saveLocation(PointF location) {
        // The location saves each time the player uses a teleport
        // But as this is a rogue-like if the player quits or dies then they need to start again
        restartX = location.x;
        restartY = location.y;
    }

    public PointF loadLocation() {
        // Used every time the player loses a life
        return new PointF(restartX, restartY);

    }

    public int getLives(){
        return lives;
    }

    public int getFireRate(){
        return mgFireRate;
    }
	
	public void increaseFireRate(){
		mgFireRate += 2;
	}


    public void gotCredit(){
        numCredits ++;
    }

    public int getCredits(){
        return numCredits;
    }

    public void loseLife(){
        lives--;
    }

    public void addLife(){
        lives++;
    }

    public void resetLives(){
        lives = 3;
    }

    public void resetCredits(){
        lives = 0;
    }


}
