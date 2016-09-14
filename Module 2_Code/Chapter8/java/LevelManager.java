package com.gamecodeschool.c8platformgame;

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
    ArrayList<Background> backgrounds;

    ArrayList<Rect> currentButtons;
    Bitmap[] bitmapsArray;

    public LevelManager(Context context, int pixelsPerMetre, int screenWidth, InputController ic, String level, float px, float py) {
        this.level = level;

        switch (level) {
            case "LevelCave":
                levelData = new LevelCave();
                break;

            // We can add extra levels here
            case "LevelCity":
                levelData = new LevelCity();
                break;

            case "LevelForest":
                levelData = new LevelForest();
                break;

            case "LevelMountain":
                levelData = new LevelMountain();
                break;

        }

        // To hold all our GameObjects
        gameObjects = new ArrayList<>();

        // To hold 1 of every Bitmap
        bitmapsArray = new Bitmap[25];

        // Load all the GameObjects and Bitmaps
        loadMapData(context, pixelsPerMetre, px, py);
        loadBackgrounds(context, pixelsPerMetre, screenWidth);

        // Set waypoints for our guards
        setWaypoints();

        //playing = true;
    }

    private void loadBackgrounds(Context context, int pixelsPerMetre, int screenWidth) {
        backgrounds = new ArrayList<Background>();
        //load the background data into the Background objects and
        // place them in our GameObject arraylist
        for (BackgroundData bgData : levelData.backgroundDataList) {
            backgrounds.add(new Background(context, pixelsPerMetre, screenWidth, bgData));
        }

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

            case 'f':
                index = 8;
                break;

            case '2':
                index = 9;
                break;

            case '3':
                index = 10;
                break;

            case '4':
                index = 11;
                break;

            case '5':
                index = 12;
                break;

            case '6':
                index = 13;
                break;

            case '7':
                index = 14;
                break;

            case 'w':
                index = 15;
                break;

            case 'x':
                index = 16;
                break;

            case 'l':
                index = 17;
                break;

            case 'r':
                index = 18;
                break;

            case 's':
                index = 19;
                break;

            case 'm':
                index = 20;
                break;

            case 'z':
                index = 21;
                break;

            case 't':
                index = 22;
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

            case 'f':
                index = 8;
                break;

            case '2':
                index = 9;
                break;

            case '3':
                index = 10;
                break;

            case '4':
                index = 11;
                break;

            case '5':
                index = 12;
                break;

            case '6':
                index = 13;
                break;

            case '7':
                index = 14;
                break;

            case 'w':
                index = 15;
                break;

            case 'x':
                index = 16;
                break;

            case 'l':
                index = 17;
                break;

            case 'r':
                index = 18;
                break;

            case 's':
                index = 19;
                break;

            case 'm':
                index = 20;
                break;

            case 'z':
                index = 21;
                break;

            case 't':
                index = 22;
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
        int teleportIndex = -1;
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

                        case 'f':
                            // Add a fire tile the gameObjects
                            gameObjects.add(new Fire(context, j, i, c, pixelsPerMetre));
                            break;

                        case '2':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Snow(j, i, c));
                            break;

                        case '3':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Brick(j, i, c));
                            break;

                        case '4':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Coal(j, i, c));
                            break;

                        case '5':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Concrete(j, i, c));
                            break;

                        case '6':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Scorched(j, i, c));
                            break;

                        case '7':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Stone(j, i, c));
                            break;

                        case 'w':
                            // Add a tree to the gameObjects
                            gameObjects.add(new Tree(j, i, c));
                            break;

                        case 'x':
                            // Add a tree2 to the gameObjects
                            gameObjects.add(new Tree2(j, i, c));
                            break;

                        case 'l':
                            // Add a tree to the gameObjects
                            gameObjects.add(new Lampost(j, i, c));
                            break;

                        case 'r':
                            // Add a stalactite to the gameObjects
                            gameObjects.add(new Stalactite(j, i, c));
                            break;

                        case 's':
                            // Add a stalagmite to the gameObjects
                            gameObjects.add(new Stalagmite(j, i, c));
                            break;

                        case 'm':
                            // Add a cart to the gameObjects
                            gameObjects.add(new Cart(j, i, c));
                            break;

                        case 'z':
                            // Add a boulders to the gameObjects
                            gameObjects.add(new Boulders(j, i, c));
                            break;

                        case 't':
                            // Add a teleport to the gameObjects
                            teleportIndex++;
                            gameObjects.add(new Teleport(j, i, c, levelData.locations.get(teleportIndex)));
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
