package com.gamecodeschool.c7platformgame;

import android.graphics.Rect;

public class Viewport {

    private Vector2Point5D currentViewportWorldCentre;
    private Rect convertedRect;
    private int pixelsPerMetreX;
    private int pixelsPerMetreY;
    private int screenXResolution;
    private int screenYResolution;
    private int screenCentreX;
    private int screenCentreY;
    private int metresToShowX;
    private int metresToShowY;
    private int numClipped;

    Viewport(int x, int y){

        screenXResolution = x;
        screenYResolution = y;

        screenCentreX = screenXResolution / 2;
        screenCentreY = screenYResolution / 2;

        pixelsPerMetreX = screenXResolution / 32;
        pixelsPerMetreY = screenYResolution / 18;

        metresToShowX = 34;
        metresToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCentre = new Vector2Point5D();

    }


    void setWorldCentre(float x, float y){
        currentViewportWorldCentre.x  = x;
        currentViewportWorldCentre.y  = y;

    }


    public int getScreenWidth(){

        return  screenXResolution;
    }

    public int getScreenHeight(){

        return  screenYResolution;
    }

    public int getPixelsPerMetreX(){

        return  pixelsPerMetreX;
    }

    public Rect worldToScreen(float objectX, float objectY, float objectWidth, float objectHeight){
        int left = (int) (screenCentreX - ((currentViewportWorldCentre.x - objectX) * pixelsPerMetreX));
        int top =  (int) (screenCentreY - ((currentViewportWorldCentre.y - objectY) * pixelsPerMetreY));
        int right = (int) (left + (objectWidth * pixelsPerMetreX));
        int bottom = (int) (top + (objectHeight * pixelsPerMetreY));
        convertedRect.set(left, top, right, bottom);
        return convertedRect;
    }

    public boolean clipObjects(float objectX, float objectY, float objectWidth, float objectHeight) {
        boolean clipped = true;

        if (objectX - objectWidth < currentViewportWorldCentre.x + (metresToShowX / 2)) {
            if (objectX + objectWidth> currentViewportWorldCentre.x - (metresToShowX / 2)) {
                if (objectY - objectHeight< currentViewportWorldCentre.y + (metresToShowY / 2)) {
                    if (objectY + objectHeight > currentViewportWorldCentre.y - (metresToShowY / 2)){
                        clipped = false;
                    }

                }
            }
        }

        //for debugging
        if(clipped){
            numClipped++;
        }

        return clipped;
    }

    public int getNumClipped(){

        return numClipped;
    }

    public void resetNumClipped(){

        numClipped = 0;
    }

}

