package com.gamecodeschool.c11asteroids;

import android.graphics.PointF;

// All objects which can collide have a collision package.
// Asteroids, ship, bullets. The structure seems like slight
// overkill for bullets but it keeps the code generic,
// and the use of vertexListLength means there isn't any
// actual speed overhead. Also if we wanted line, triangle or
// even spinning bullets the code wouldn't need to change.

public class CollisionPackage {

    // All the members are public to avoid multiple calls
    // to getters and setters.

    // The facing angle allows us to calculate the
    // current world coordinates of each vertex using
    // the model-space coordinates in vertexList.
    public float facingAngle;

    // The model-space coordinates
    public PointF[] vertexList;

    // The number of vertices in vertexList
    // is kept in this next int because it is pre-calculated
    // and we can use it in our loops instead of
    // continually calling vertexList.length.
    public int vertexListLength;

    // Where is the centre of the object?
    public PointF worldLocation;

    // This next float will be used to detect if the circle shaped
    // hitboxes collide. It represents the furthest point
    // from the centre of any given object.
    // Each object will set this slightly differently.
    // The ship will use height/2 an asteroid will use 25
    // To allow for a max length rotated coordinate.
    public float radius;

    // A couple of points to store results and avoid creating new
    // objects during intensive collision detection
    public PointF currentPoint = new PointF();
    public PointF currentPoint2 = new PointF();

    public CollisionPackage(PointF[] vertexList, PointF worldLocation,
                            float radius, float facingAngle){

        vertexListLength = vertexList.length;
        this.vertexList = new PointF[vertexListLength];
        // Make a copy of the array

        for (int i = 0; i < vertexListLength; i++) {
            this.vertexList[i] = new PointF();
            this.vertexList[i].x = vertexList[i].x;
            this.vertexList[i].y = vertexList[i].y;
        }


        //this.vertexList = vertexList;

        this.worldLocation = new PointF();
        this.worldLocation = worldLocation;

        this.radius = radius;

        this.facingAngle = facingAngle;

    }


}
