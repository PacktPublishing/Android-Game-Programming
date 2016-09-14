package com.gamecodeschool.c7platformgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

public class LevelManager {

    private String level;
    int mapWidth;
    int mapHeight;

    Player player;
    int playerIndex;

    private boolean playing;
    float gravity;

    LevelData levelData;
    ArrayList<GameObject> gameObjects;

    ArrayList<Rect> currentButtons;
    Bitmap[] bitmapsArray;

    public LevelManager(Context context, int pixelsPerMetre, int screenWidth, InputController ic, String level, float px, float py) {
        this.level = level;

        switch (level) {
            case "LevelCave":
                levelData = new LevelCave();
                break;

            // We can add extra levels here

        }

        // To hold all our GameObjects
        gameObjects = new ArrayList<>();

        // To hold 1 of every Bitmap
        bitmapsArray = new Bitmap[25];

        // Load all the GameObjects and Bitmaps
        loadMapData(context, pixelsPerMetre, px, py);

        // Set waypoints for our guards
        setWaypoints();

        //playing = true;
    }

    public void setWaypoints() {
        // Loop through all game objects looking for Guards
        for (GameObject guard : this.gameObjects) {
            if (guard.getType() == 'g') {
                // Set waypoints for this guard
                // find the tile beneath the guard
                // this relies on the designer putting the guard in sensible location

                int startTileIndex = -1;
                int startGuardIndex = 0;
                float waypointX1 = -1;
                float waypointX2 = -1;
                //Log.d("yay","found a guard");
                //Log.d("before fors x1 = ", "" + waypointX1);
                //Log.d("before fors x2 = ", "" + waypointX2);

                for (GameObject tile : this.gameObjects) {
                    startTileIndex++;
                    if (tile.getWorldLocation().y == guard.getWorldLocation().y + 2) {
                        //tile is two space below current guard
                        // Now see if has same x coordinate
                        if (tile.getWorldLocation().x == guard.getWorldLocation().x) {

                            // Found the tile the guard is "standing" on
                            // Now go left as far as possible before non travers-able tile is found
                            // Either on guards row or tile row
                            // upto a maximum of 5 tiles. (5 is arbitrary value)
                            for (int i = 0; i < 5; i++) {// left for loop

                                if (!gameObjects.get(startTileIndex - i).isTraversable()) {
                                    //set the left waypoint
                                    waypointX1 = gameObjects.get(startTileIndex - (i + 1)).getWorldLocation().x;
                                    Log.d("set x1 = ", "" + waypointX1);
                                    break;// Leave left for loop

                                } else {
                                    //set to max 5 tiles as no non traversible tile found
                                    waypointX1 = gameObjects.get(startTileIndex - 5).getWorldLocation().x;
                                }
                            }// end get left waypoint

                            for (int i = 0; i < 5; i++) {// right for loop
                                if (!gameObjects.get(startTileIndex + i).isTraversable()) {
                                 //set the right waypoint

                                    waypointX2 = gameObjects.get(startTileIndex + (i - 1)).getWorldLocation().x;
                                    //Log.d("set x2 = ", "" + waypointX2);
                                    break;// Leave right for loop

                                } else {
                                    //set to max 5 tiles away
                                    waypointX2 = gameObjects.get(startTileIndex + 5).getWorldLocation().x;
                                }

                            }// end get right waypoint
                            Guard g = (Guard) guard;

                            g.setWaypoints(waypointX1, waypointX2);
                            //Log.d("after fors x1 = ", "" + waypointX1);
                        }

                    }
                }
            }
        }
    }

    public void switchPlayingStatus() {
        playing = !playing;
        if (playing) {
            gravity = 6;
        } else {
            gravity = 0;
        }
    }


    public boolean isPlaying() {
        return playing;
    }


    // Each index Corresponds to a bitmap
    public Bitmap getBitmap(char blockType) {

        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;

            case '1':
                index = 1;
                break;

            case 'p':
                index = 2;
                break;

            case 'c':
                index = 3;
                break;

            case 'u':
                index = 4;
                break;

            case 'e':
                index = 5;
                break;

            case 'd':
                index = 6;
                break;

            case 'g':
                index = 7;
                break;

            default:
                index = 0;
                break;
        }

        return bitmapsArray[index];
    }

    // This method allows each GameObject which 'knows'
    // its type to get the correct index to its Bitmap
    // in the Bitmap array.
    public int getBitmapIndex(char blockType) {

        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;

            case '1':
                index = 1;
                break;

            case 'p':
                index = 2;
                break;

            case 'c':
                index = 3;
                break;

            case 'u':
                index = 4;
                break;

            case 'e':
                index = 5;
                break;

            case 'd':
                index = 6;
                break;

            case 'g':
                index = 7;
                break;

            default:
                index = 0;
                break;
        }

        return index;
    }

    // For now we just load all the grass tiles
    // and the player. Soon we will have many GameObjects
    void loadMapData(Context context, int pixelsPerMetre, float px, float py) {

        char c;

        //Keep track of where we load our game objects
        int currentIndex = -1;

        // how wide and high is the map? Viewport needs to know
        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for (int i = 0; i < levelData.tiles.size(); i++) {
            for (int j = 0; j < levelData.tiles.get(i).length(); j++) {

                c = levelData.tiles.get(i).charAt(j);
                if (c != '.') {// Don't want to load the empty spaces
                    currentIndex++;
                    switch (c) {

                        case '1':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Grass(j, i, c));
                            break;

                        case 'p':// a player
                            // Add a player to the gameObjects
                            gameObjects.add(new Player
                                    (context, px, py, pixelsPerMetre));

                            // We want the index of the player
                            playerIndex = currentIndex;
                            // We want a reference to the player object
                            player = (Player) gameObjects.get(playerIndex);

                            break;

                        case 'c':
                            // Add a coin to the gameObjects
                            gameObjects.add(new Coin(j, i, c));
                            break;

                        case 'u':
                            // Add a machine gun upgrade to the gameObjects
                            gameObjects.add(new MachineGunUpgrade(j, i, c));
                            break;

                        case 'e':
                            // Add an extra life to the gameObjects
                            gameObjects.add(new ExtraLife(j, i, c));
                            break;

                        case 'd':
                            // Add a drone to the gameObjects
                            gameObjects.add(new Drone(j, i, c));
                            break;

                        case 'g':
                            // Add a guard to the gameObjects
                            gameObjects.add(new Guard(context, j, i, c, pixelsPerMetre));
                            break;


                    }

                    // If the bitmap isn't prepared yet
                    if (bitmapsArray[getBitmapIndex(c)] == null) {
                        // Prepare it now and put it in the bitmapsArrayList
                        bitmapsArray[getBitmapIndex(c)] =
                                gameObjects.get(currentIndex).
                                        prepareBitmap(context,
                                                gameObjects.get(currentIndex).
                                                        getBitmapName(),
                                                pixelsPerMetre);

                    }
                }
            }
        }
    }

}
