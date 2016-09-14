package com.gamecodeschool.c9asteroids;

public class GameManager {

    int mapWidth = 600;
    int mapHeight = 600;
    private boolean playing = false;

    // Our first game object
    SpaceShip ship;

    int screenWidth;
    int screenHeight;

    // How many metres of our virtual world
    // we will show on screen at any time.
    int metresToShowX = 390;
    int metresToShowY = 220;

    public GameManager(int x, int y){

        screenWidth = x;
        screenHeight = y;

    }

    public void switchPlayingStatus() {
        playing = !playing;

    }

    public boolean isPlaying(){
        return playing;
    }
}
