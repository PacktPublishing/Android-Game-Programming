package com.gamecodeschool.c6platformgame;

import android.content.Context;

public class Player extends GameObject {
    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;

    final float MAX_X_VELOCITY = 10;

    public boolean isFalling;
    private boolean isJumping;
    private long jumpTime;
    private long maxJumpTime = 700;// jump 7 10ths of second half up half down

    boolean isPressingRight = false;
    boolean isPressingLeft = false;

    Player(Context context, float worldStartX, float worldStartY, int pixelsPerMetre) {

        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT); // 2 metres tall
        setWidth(WIDTH); // 1 metre wide

        // Standing still to start with
        setxVelocity(0);
        setyVelocity(0);
        setFacing(LEFT);
        isFalling = false;

        // Now for the player's other attributes
        // Our game engine will use these
        setMoves(true);
        setActive(true);
        setVisible(true);

        setType('p');

        // Choose a Bitmap
        // This is a prite sheet with multiple frames
        // of animation. SO it will look silly until we animate it
        // In chapter 6.
        setBitmapName("player");

        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT = 5;

        // Set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMetre, true);


        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);

        rectHitboxFeet = new RectHitbox();
        rectHitboxHead = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();
    }

    public void update(long fps, float gravity) {
        if (isPressingRight) {
            this.setxVelocity(MAX_X_VELOCITY);
        } else if (isPressingLeft) {
            this.setxVelocity(-MAX_X_VELOCITY);
        } else {
            this.setxVelocity(0);
        }

        //what way is player facing?
        if (this.getxVelocity() > 0) {
            //facing right
            setFacing(RIGHT);
        } else if (this.getxVelocity() < 0) {
            //facing left
            setFacing(LEFT);
        }//if 0 then unchanged


        // Jumping and gravity
        if (isJumping) {
            long timeJumping = System.currentTimeMillis() - jumpTime;
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    this.setyVelocity(-gravity);//on the way up
                } else if (timeJumping > maxJumpTime / 2) {
                    this.setyVelocity(gravity);//going down
                }
            } else {
                isJumping = false;

            }
        } else {
            this.setyVelocity(gravity);
            // Read Me
            // Remove this next line to make the game easier
            // it means the long jumps are less punishing
            // because the player can take off just after the platform
            // but it also means they might be able to cheat by jumping in thin air
            isFalling = true;
        }

        // Let's go!
        this.move(fps);

        // Update all the hitboxes to the new location
        // Get the current world location of the player
        // and save them as local variables we will use next
        Vector2Point5D location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        //update the player feet hitbox
        rectHitboxFeet.top = ly + (getHeight() * .95f);
        rectHitboxFeet.left = lx + getWidth() * .2f;
        rectHitboxFeet.bottom = ly + getHeight() * .98f;
        rectHitboxFeet.right = lx + getWidth() * .8f;

        // Update player head hitbox
        rectHitboxHead.top = ly;
        rectHitboxHead.left = lx + (getWidth() * .4f);
        rectHitboxHead.bottom = ly + getHeight() * .2f;
        rectHitboxHead.right = lx + (getWidth() * .6f);

        // Update player left hitbox
        rectHitboxLeft.top = ly + getHeight() * .2f;
        rectHitboxLeft.left = lx + getWidth() * .2f;
        rectHitboxLeft.bottom = ly + getHeight() * .8f;
        rectHitboxLeft.right = lx + (getWidth() * .3f);

        // Update player right hitbox
        rectHitboxRight.top = ly + getHeight() * .2f;
        rectHitboxRight.left = lx + (getWidth() * .8f);
        rectHitboxRight.bottom = ly + getHeight() * .8f;
        rectHitboxRight.right = lx + getWidth() * .7f;
    }

    public int checkCollisions(RectHitbox rectHitbox) {
        int collided = 0;//no collision

        //the left
        if (this.rectHitboxLeft.intersects(rectHitbox)) {
            //left has collided
            //move player just to right of current hitbox
            this.setWorldLocationX(rectHitbox.right - getWidth() * .2f);
            //setxVelocity(0);
            //setPressingLeft(false);
            collided = 1;
        }

        //the right
        if (this.rectHitboxRight.intersects(rectHitbox)) {
            //right has collided
            //move player just to left of current hitbox
            this.setWorldLocationX(rectHitbox.left - getWidth() * .8f);
            //setxVelocity(0);
            //setPressingRight(false);
            collided = 1;
        }

        //the feet
        if (this.rectHitboxFeet.intersects(rectHitbox)) {
            //feet have collided
            //move feet to just above current hitbox
            this.setWorldLocationY(rectHitbox.top - getHeight());
            collided = 2;
            //isFalling = false;
        }

        //Now the head
        if (this.rectHitboxHead.intersects(rectHitbox)) {
            //head has collided
            //move head to just below current hitbox bottom
            this.setWorldLocationY(rectHitbox.bottom);
            collided = 3;
        }

        return collided;

    }

    public void setPressingRight(boolean isPressingRight) {
        this.isPressingRight = isPressingRight;
    }

    public void setPressingLeft(boolean isPressingLeft) {
        this.isPressingLeft = isPressingLeft;
    }

    public void startJump(SoundManager sm) {
        if (!isFalling) {//can't jump if falling
            if (!isJumping) {//not already jumping
                isJumping = true;
                jumpTime = System.currentTimeMillis();
                sm.playSound("jump");
            }
        }
    }

}