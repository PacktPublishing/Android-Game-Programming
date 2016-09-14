package com.gamecodeschool.c11asteroids;

import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

public class AsteroidsRenderer implements Renderer {

    // Are we debugging at the moment
    boolean debugging = true;

    // For monitoring and controlling the frames per second
    long frameCounter = 0;
    long averageFPS = 0;
    private long fps;

    // For converting each game world coordinate
    // into a GL space coordinate (-1,-1 to 1,1)
    // for drawing on the screen
    private final float[] viewportMatrix = new float[16];

    // A class to help manage our game objects
    // current state.
    private GameManager gm;
    private SoundManager sm;
    private InputController ic;

    // For capturing various PointF details without
    // creating new objects in the speed critical areas
    PointF handyPointF;
    PointF handyPointF2;

    // This will hold our game buttons
    private final GameButton[] gameButtons = new GameButton[5];

    public AsteroidsRenderer(GameManager gameManager,
                             SoundManager soundManager,
                             InputController inputController) {

        gm = gameManager;
        sm = soundManager;
        ic = inputController;

        handyPointF = new PointF();
        handyPointF2 = new PointF();


    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

        // The color that will be used to clear the
        // screen each frame in onDrawFrame()
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Get our GLManager to compile and link the shaders into an object
        GLManager.buildProgram();


        createObjects();

    }


    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {

        // Make full screen
        glViewport(0, 0, width, height);

        /*
            Initialize our viewport matrix by passing in the starting
            range of the game world that will be mapped, by OpenGL to
            the screen. We will dynamically amend this as the player
            moves around.

            The arguments to setup the viewport matrix:
            our array,
            starting index in array,
            min x, max x,
            min y, max y,
            min z, max z)
        */

        orthoM(viewportMatrix, 0, 0, gm.metresToShowX, 0, gm.metresToShowY, 0f, 1f);
    }

    private void createObjects() {
        // Create our game objects

        // First the ship in the center of the map
        gm.ship = new SpaceShip(gm.mapWidth / 2, gm.mapHeight / 2);

        // The deadly border
        gm.border = new Border(gm.mapWidth, gm.mapHeight);

        // Some stars
        gm.stars = new Star[gm.numStars];
        for (int i = 0; i < gm.numStars; i++) {

            // Pass in the map size so the stars no where to spawn
            gm.stars[i] = new Star(gm.mapWidth, gm.mapHeight);
        }

        // Some bullets
        gm.bullets = new Bullet[gm.numBullets];
        for (int i = 0; i < gm.numBullets; i++) {
            gm.bullets[i] = new Bullet(
                    gm.ship.getWorldLocation().x,
                    gm.ship.getWorldLocation().y);
        }

        // Determine the number of asteroids
        gm.numAsteroids = gm.baseNumAsteroids * gm.levelNumber;
        // Set how many asteroids need to be destroyed by player
        gm.numAsteroidsRemaining = gm.numAsteroids;
        // Spawn the asteroids
        //gm.asteroids = new Asteroid[gm.baseNumAsteroids];
        for (int i = 0; i < gm.numAsteroids * gm.levelNumber; i++) {

            // Create a new asteroid
            // Pass in level number so they can be made
            // appropriately dangerous.
            gm.asteroids[i] = new Asteroid(gm.levelNumber, gm.mapWidth, gm.mapHeight);
        }

        // Now for the HUD objects
        // First the life icons
        for(int i = 0; i < gm.numLives; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gm.lifeIcons[i] = new LifeIcon(gm, i);
        }



        // Now the tally icons (1 at the start)
        for(int i = 0; i < gm.numAsteroidsRemaining; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gm.tallyIcons[i] = new TallyIcon(gm, i);
        }


        // Now the buttons
        ArrayList<Rect> buttonsToDraw = ic.getButtons();
        int i = 0;
        for (Rect rect : buttonsToDraw) {
            gameButtons[i] = new GameButton(rect.top, rect.left, rect.bottom, rect.right, gm);
            i++;
        }

    }


    @Override
    public void onDrawFrame(GL10 glUnused) {

        long startFrameTime = System.currentTimeMillis();

        if (gm.isPlaying()) {
            update(fps);
        }

        draw();

        // Calculate the fps this frame
        // We can then use the result to
        // time animations and more.
        long timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame;
        }

        // Output the average frames per second to the console
        if (debugging) {
            frameCounter++;
            averageFPS = averageFPS + fps;
            if (frameCounter > 100) {
                averageFPS = averageFPS / frameCounter;
                frameCounter = 0;
                Log.e("averageFPS:", "" + averageFPS);
            }
        }
    }

    public void lifeLost(){
        // Reset the ship to the centre
        gm.ship.setWorldLocation(gm.mapWidth/2, gm.mapHeight/2);
        // Play a sound
        sm.playSound("shipexplode");

        // Deduct a life
        gm.numLives = gm.numLives -1;


        if(gm.numLives == 0){
            gm.levelNumber = 1;
            gm.numLives = 3;
            createObjects();
            gm.switchPlayingStatus();
            sm.playSound("gameover");
        }
    }

    public void destroyAsteroid(int asteroidIndex){

        gm.asteroids[asteroidIndex].setActive(false);
        // Play a sound
        sm.playSound("explode");
        // Reduce the number of active asteroids
        gm.numAsteroidsRemaining --;

        // Has the player cleared them all?
        if(gm.numAsteroidsRemaining == 0){
            // Play a victory sound

            // Increment the level number
            gm.levelNumber ++;

            // Extra life
            gm.numLives ++;

            sm.playSound("nextlevel");
            // Respawn everything
            // With more asteroids
            createObjects();


        }
    }


    private void update(long fps) {

        // Update (twinkle) the stars
        for (int i = 0; i < gm.numStars; i++) {
            gm.stars[i].update();
        }

        // Update all the bullets
        for (int i = 0; i < gm.numBullets; i++) {

            // If not in flight they will need the ships location
            gm.bullets[i].update(fps, gm.ship.getWorldLocation());

        }

        // Run the ship update() method
        gm.ship.update(fps);

        // Update all the asteroids
        for (int i = 0; i < gm.numAsteroids; i++) {
            if (gm.asteroids[i].isActive()) {
                gm.asteroids[i].update(fps);
            }
        }

        // End of all updates!!

        // All objects are in their new locations
        // Start collision detection

        // Check if the ship needs containing
        if (CD.contain(gm.mapWidth, gm.mapHeight, gm.ship.cp)) {

            lifeLost();

        }


        // Check if an asteroid needs containing
        for (int i = 0; i < gm.numAsteroids; i++) {
            if (gm.asteroids[i].isActive()) {
                if (CD.contain(gm.mapWidth, gm.mapHeight, gm.asteroids[i].cp)) {

                    // Bounce the asteroid back into the game
                    gm.asteroids[i].bounce();

                    // Play a sound
                    sm.playSound("blip");


                }
            }


        }


        // Check if bullet needs containing
        // But first see if the bullet is out of sight
        // If it is reset it to make game harder
        for (int i = 0; i < gm.numBullets; i++) {

            // Is the bullet in flight?
            if (gm.bullets[i].isInFlight()) {

                // Comment the next block to make the game easier!!!
                // It will allow the bullets to go all the way from
                // ship to border without being reset. These lines reset the bullet when
                // shortly after they leave the players view.
                // This forces the player to go 'hunting' for the
                // asteroids instead of spinning round spamming the
                // fire button...
                // This code would be better with a viewport.clip() method
                // like in project 2 but seems a bit excessive just for these
                // few 15ish lines of code.

                // Start comment out to make easier
                handyPointF = gm.bullets[i].getWorldLocation();
                handyPointF2 = gm.ship.getWorldLocation();
                if(handyPointF.x > handyPointF2.x + gm.metresToShowX / 2){
                    // Reset the bullet
                    gm.bullets[i].resetBullet(gm.ship.getWorldLocation());
                }else
                if(handyPointF.x < handyPointF2.x - gm.metresToShowX / 2){
                    // Reset the bullet
                    gm.bullets[i].resetBullet(gm.ship.getWorldLocation());
                }else
                if(handyPointF.y > handyPointF2.y + gm.metresToShowY/ 2){
                    // Reset the bullet
                    gm.bullets[i].resetBullet(gm.ship.getWorldLocation());
                }else
                if(handyPointF.y < handyPointF2.y - gm.metresToShowY / 2){
                    // Reset the bullet
                    gm.bullets[i].resetBullet(gm.ship.getWorldLocation());
                }
                // End comment out to make easier

                // Does bullet need containing?
                if (CD.contain(gm.mapWidth, gm.mapHeight, gm.bullets[i].cp)) {

                    // Reset the bullet
                    gm.bullets[i].resetBullet(gm.ship.getWorldLocation());
                    // Play a sound
                    sm.playSound("ricochet");
                }

            }

        }

        // Now we see if anything has hit an asteroid

        // Check collisions between asteroids and bullets
        // Loop through each bullet and asteroid in turn
        for (int bulletNum = 0; bulletNum < gm.numBullets; bulletNum++) {
            for (int asteroidNum = 0; asteroidNum < gm.numAsteroids; asteroidNum++) {

                // Check that the current bullet is in flight
                // and the current asteroid is active before proceeding
                if (gm.bullets[bulletNum].isInFlight() && gm.asteroids[asteroidNum].isActive())

                    // Perform the collision checks by passing in the collision packages

                    // A Bullet only has one vertex. Our collision detection works on vertex pairs

                    if (CD.detect(gm.bullets[bulletNum].cp, gm.asteroids[asteroidNum].cp)) {

                        // If we get a hit...
                        destroyAsteroid(asteroidNum);
                        // Reset the bullet
                        gm.bullets[bulletNum].resetBullet(gm.ship.getWorldLocation());
                    }
            }
        }

        // Check collisions between asteroids and ship
        // Loop through each asteroid in turn
        for (int asteroidNum = 0; asteroidNum < gm.numAsteroids; asteroidNum++) {

            // Is the current asteroid active before proceeding
            if (gm.asteroids[asteroidNum].isActive()) {

                // Perform the collision checks by passing in the collision packages
                if (CD.detect(gm.ship.cp, gm.asteroids[asteroidNum].cp)) {

                    // hit!
                    destroyAsteroid(asteroidNum);
                    lifeLost();
                }
            }
        }


    }


    private void draw() {

        // Where is the ship?
        handyPointF = gm.ship.getWorldLocation();

        // Modify the viewport matrix orthographic projection
        // based on the ship location
        orthoM(viewportMatrix, 0,
                handyPointF.x - gm.metresToShowX / 2,
                handyPointF.x + gm.metresToShowX / 2,
                handyPointF.y - gm.metresToShowY / 2,
                handyPointF.y + gm.metresToShowY / 2,
                0f, 1f);

        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT);

        // Start drawing!

        // Some stars
        for (int i = 0; i < gm.numStars; i++) {
            // Draw the star if it is active
            if(gm.stars[i].isActive()) {
                gm.stars[i].draw(viewportMatrix);
            }
        }

        // The bullets
        for (int i = 0; i < gm.numBullets; i++) {
            gm.bullets[i].draw(viewportMatrix);
        }

        for (int i = 0; i < gm.numAsteroids; i++) {
            if (gm.asteroids[i].isActive()) {
                gm.asteroids[i].draw(viewportMatrix);
            }
        }




        // Draw the ship
        gm.ship.draw(viewportMatrix);
        gm.border.draw(viewportMatrix);

        // the buttons
        for (int i = 0; i < gameButtons.length; i++) {
            gameButtons[i].draw();
        }

        // Draw the life icons
        for(int i = 0; i < gm.numLives; i++) {
            gm.lifeIcons[i].draw();
        }

        // Draw the level icons
        for(int i = 0; i < gm.numAsteroidsRemaining; i++) {
            gm.tallyIcons[i].draw();
        }

    }

}