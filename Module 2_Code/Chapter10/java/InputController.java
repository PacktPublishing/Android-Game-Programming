package com.gamecodeschool.c10asteroids;

import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class InputController {

    private int currentBullet;

    Rect left;
    Rect right;
    Rect thrust;
    Rect shoot;
    Rect pause;

    InputController(int screenWidth, int screenHeight) {

        //Configure the player buttons
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;

        left = new Rect(buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth,
                screenHeight - buttonPadding);

        right = new Rect(buttonWidth + buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth + buttonPadding + buttonWidth,
                screenHeight - buttonPadding);

        thrust = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding - buttonHeight - buttonPadding);

        shoot = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding);

        pause = new Rect(screenWidth - buttonPadding - buttonWidth,
                buttonPadding,
                screenWidth - buttonPadding,
                buttonPadding + buttonHeight);



    }

    public ArrayList getButtons(){
        //create an array of buttons for the draw method
        ArrayList<Rect> currentButtonList = new ArrayList<>();
        currentButtonList.add(left);
        currentButtonList.add(right);
        currentButtonList.add(thrust);
        currentButtonList.add(shoot);
        currentButtonList.add(pause);
        return  currentButtonList;
    }



    public void handleInput(MotionEvent motionEvent,GameManager l, SoundManager sound){
        int pointerCount = motionEvent.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {

            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);



                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        if (right.contains(x, y)) {
                            l.ship.setPressingRight(true);
                            l.ship.setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            l.ship.setPressingLeft(true);
                            l.ship.setPressingRight(false);
                        } else if (thrust.contains(x, y)) {
                            l.ship.toggleThrust();
                        } else if (shoot.contains(x, y)) {
                            if (l.ship.pullTrigger()) {
                                l.bullets[currentBullet].shoot(l.ship.getFacingAngle());
                                currentBullet++;
                                // If we are on the last bullet restart
                                // from the first one again
                                if(currentBullet == l.numBullets){
                                    currentBullet = 0;
                                }
                                sound.playSound("shoot");
                            }
                        } else if (pause.contains(x, y)) {
                            l.switchPlayingStatus();

                        }

                        break;


                    case MotionEvent.ACTION_UP:
                        if (right.contains(x, y)) {
                            l.ship.setPressingRight(false);
                        } else if (left.contains(x, y)) {
                            l.ship.setPressingLeft(false);
                        }


                        break;


                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (right.contains(x, y)) {
                            l.ship.setPressingRight(true);
                            l.ship.setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            l.ship.setPressingLeft(true);
                            l.ship.setPressingRight(false);
                        } else if (thrust.contains(x, y)) {
                            l.ship.toggleThrust();
                        } else if (shoot.contains(x, y)) {
                            if (l.ship.pullTrigger()) {
                                l.bullets[currentBullet].shoot(l.ship.getFacingAngle());
                                currentBullet++;
                                // If we are on the last bullet restart
                                // from the first one again
                                if(currentBullet == l.numBullets){
                                    currentBullet = 0;
                                }
                                sound.playSound("shoot");
                            }
                        } else if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                        }
                        break;


                    case MotionEvent.ACTION_POINTER_UP:
                        if (right.contains(x, y)) {
                            l.ship.setPressingRight(false);
                        } else if (left.contains(x, y)) {
                            l.ship.setPressingLeft(false);
                        }
                        break;
                }
            }







    }
}
