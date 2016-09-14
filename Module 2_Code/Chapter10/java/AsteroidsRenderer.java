package com.gamecodeschool.c10asteroids;

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
        for (int i = 0; i < gm.numLives; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gm.lifeIcons[i] = new LifeIcon(gm, i);
        }


        // Now the tally icons (1 at the start)
        for (int i = 0; i < gm.numAsteroidsRemaining; i++) {
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
            if (gm.stars[i].isActive()) {
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
        for (int i = 0; i < gm.numLives; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gm.lifeIcons[i].draw();
        }

        // Draw the level icons
        for (int i = 0; i < gm.numAsteroidsRemaining; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gm.tallyIcons[i].draw();
        }

    }

}