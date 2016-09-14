package com.gamecodeschool.c11asteroids;

import android.graphics.PointF;

public class CD {

    private static PointF rotatedPoint = new PointF();

    public static boolean detect(CollisionPackage cp1, CollisionPackage cp2) {
        boolean collided = false;

        // Check circle collision between the two objects

        // Get the distance of the two objects from
        // the centre of the circles on the x axis
        float distanceX = (cp1.worldLocation.x)
                - (cp2.worldLocation.x);

        // Get the distance of the two objects from
        // the centre of the circles on the y axis
        float distanceY = (cp1.worldLocation.y)
                - (cp2.worldLocation.y);

        // Calculate the distance between the center of each circle
        double distance = Math.sqrt
                (distanceX * distanceX + distanceY * distanceY);

        // Finally see if the two circles overlap
        // If they do it is worth doing the more intensive
        // and accurate check.
        if (distance < cp1.radius + cp2.radius) {
            // Before we jump into the for loops we will compute a few things
            // that won't change for the duration of this method.
            // The sine and cosine of the facing angle from each of the two
            // collision packages.


            /*
                We could make a method to rotate angles as we do this so often.
                It is not straightforward however. If we put the rotation code in a
                method we would either have to put the below sine and cosine calculations
                in it which would make it slow or pre-compute them before the method call
                and the for loops which is kind of untidy in itself.

                Also if you consider that we need to more than one value for both sine
                and cosine of an angle, the method needs to 'know' which to use, which isn't rocket
                science but it starts to get even less compact than we might have initially imagined.

                So I opted for avoiding the method call altogether even if the code is a little sprawling.

                Actually if you place the whole lot in a method call you still get
                nearly 60 fps on an old Galaxy S2 phone. So if you want to tidy things
                up, go ahead; I just thought it was worth discussing why I did
                things this way.

            */

            double radianAngle1 = ((cp1.facingAngle / 180) * Math.PI);
            double cosAngle1 = Math.cos(radianAngle1);
            double sinAngle1 = Math.sin(radianAngle1);

            double radianAngle2 = ((cp2.facingAngle / 180) * Math.PI);
            double cosAngle2 = Math.cos(radianAngle2);
            double sinAngle2 = Math.sin(radianAngle2);

            int numCrosses = 0;    // The number of times we cross a side

            float worldUnrotatedX;
            float worldUnrotatedY;

            // Loop through all the vertices from cp2 then test each in turn
            // with all the sides (vertex pairs) from cp1

            // An asteroid has an extra vertex of padding
            // which is the same as the first. So we can test the last side
            // of the asteroid. So we must always pass in the asteroid
            // collision package as the SECOND argument when calling CD.detect.
            for (int i = 0; i < cp1.vertexListLength; i++) {

                // First we need to rotate the model-space coordinate we are testing
                // to its current world position
                // First update the regular un-rotated model space coordinates
                // relative to the current world location (centre of object)
                worldUnrotatedX = cp1.worldLocation.x + cp1.vertexList[i].x;
                worldUnrotatedY = cp1.worldLocation.y + cp1.vertexList[i].y;

                // Now rotate the newly updated point, stored in currentPoint
                // around the centre point of the object (worldLocation)
                cp1.currentPoint.x = cp1.worldLocation.x + (int) ((worldUnrotatedX - cp1.worldLocation.x)
                        * cosAngle1 - (worldUnrotatedY - cp1.worldLocation.y) * sinAngle1);

                cp1.currentPoint.y = cp1.worldLocation.y + (int) ((worldUnrotatedX - cp1.worldLocation.x)
                        * sinAngle1 + (worldUnrotatedY - cp1.worldLocation.y) * cosAngle1);

                // cp1.currentPoint now hold the x/y world coordinates of the first point to test

                // Use two vertices at a time to represent the line we are testing
                // We don't test the last vertex because we are testing pairs
                // and the last vertex of cp2 is the padded extra vertex.
                // It will form part of the last side when we test vertexList[5]
                for (int j = 0; j < cp2.vertexListLength - 1; j++) {

                    // Now we get the rotated coordinates of BOTH the current 2 points being
                    // used to form a side from cp2 (the asteroid)
                    // First we need to rotate the model-space coordinate we are testing
                    // to its current world position.
                    // Update the regular un-rotated model space coordinates
                    // relative to the current world location (centre of object)
                    worldUnrotatedX = cp2.worldLocation.x + cp2.vertexList[j].x;
                    worldUnrotatedY = cp2.worldLocation.y + cp2.vertexList[j].y;

                    // Now rotate the newly updated point, stored in worldUnrotatedX/y
                    // around the centre point of the object (worldLocation)
                    cp2.currentPoint.x = cp2.worldLocation.x + (int) ((worldUnrotatedX - cp2.worldLocation.x)
                            * cosAngle2 - (worldUnrotatedY - cp2.worldLocation.y) * sinAngle2);

                    cp2.currentPoint.y = cp2.worldLocation.y + (int) ((worldUnrotatedX - cp2.worldLocation.x)
                            * sinAngle2 + (worldUnrotatedY - cp2.worldLocation.y) * cosAngle2);

                    // cp2.currentPoint now hold the x/y world coordinates of the first point that
                    // will represent a line from the asteroid

                    // No we can do exactly the same for the second vertex and store it in
                    // currentPoint2. We will then have a point and a line (two vertices)we can use the
                    // crossing number algorithm on.

                    worldUnrotatedX = cp2.worldLocation.x + cp2.vertexList[i + 1].x;
                    worldUnrotatedY = cp2.worldLocation.y + cp2.vertexList[i + 1].y;

                    // Now rotate the newly updated point, stored in worldUnrotatedX/Y
                    // around the centre point of the object (worldLocation)
                    cp2.currentPoint2.x = cp2.worldLocation.x + (int) ((worldUnrotatedX - cp2.worldLocation.x)
                            * cosAngle2 - (worldUnrotatedY - cp2.worldLocation.y) * sinAngle2);

                    cp2.currentPoint2.y = cp2.worldLocation.y + (int) ((worldUnrotatedX - cp2.worldLocation.x)
                            * sinAngle2 + (worldUnrotatedY - cp2.worldLocation.y) * cosAngle2);

                    // And now we can test the rotated point from cp1 against the
                    // rotated points which form a side from cp2

                    if (((cp2.currentPoint.y > cp1.currentPoint.y) != (cp2.currentPoint2.y > cp1.currentPoint.y)) &&
                            (cp1.currentPoint.x < (cp2.currentPoint2.x - cp2.currentPoint2.x) *
                                    (cp1.currentPoint.y - cp2.currentPoint.y) / (cp2.currentPoint2.y - cp2.currentPoint.y) + cp2.currentPoint.x))
                        numCrosses++;
                }

            }

            // So do we have a collision?
            if (numCrosses % 2 == 0) {
                // even number of crosses(outside asteroid)
                collided = false;
            } else {
                // odd number of crosses(inside asteroid)
                collided = true;
            }
        }

        return collided;
    }


    // Check if anything hits the border
    public static boolean contain(float mapWidth, float mapHeight, CollisionPackage cp) {

        boolean possibleCollision = false;

        // Check if any corner of a virtual rectangle
        // around the centre of the object is out of bounds.
        // Rectangle is best because we are testing against straight sides (the border)
        // If it is we have a possible collision.
        if (cp.worldLocation.x - cp.radius < 0) {
            possibleCollision = true;
        } else if (cp.worldLocation.x + cp.radius > mapWidth) {
            possibleCollision = true;
        } else if (cp.worldLocation.y - cp.radius < 0) {
            possibleCollision = true;
        } else if (cp.worldLocation.y + cp.radius > mapHeight) {
            possibleCollision = true;
        }

        if (possibleCollision) {
            double radianAngle = ((cp.facingAngle/180)*Math.PI);
            double cosAngle = Math.cos(radianAngle);
            double sinAngle = Math.sin(radianAngle);

            // Rotate each and every vertex then check for a collision
            // If just one is colliding then we have a collision.
            // Once we have a collision no need to check further
            for (int i = 0 ; i < cp.vertexListLength; i++){

                // First update the regular un-rotated model space coordinates
                // relative to the current world location (centre of object)
                float worldUnrotatedX = cp.worldLocation.x + cp.vertexList[i].x;
                float worldUnrotatedY =  cp.worldLocation.y + cp.vertexList[i].y;

                // Now rotate the newly updated point, stored in currentPoint
                // around the centre point of the object (worldLocation)
                cp.currentPoint.x = cp.worldLocation.x + (int) ((worldUnrotatedX - cp.worldLocation.x)
                        * cosAngle-(worldUnrotatedY - cp.worldLocation.y)* sinAngle);

                cp.currentPoint.y = cp.worldLocation.y + (int) ((worldUnrotatedX - cp.worldLocation.x)
                        * sinAngle+(worldUnrotatedY - cp.worldLocation.y)* cosAngle);

                // Check the rotated vertex for a collision
                if (cp.currentPoint.x < 0) {
                    return true;
                } else if (cp.currentPoint.x > mapWidth) {
                    return true;
                } else if (cp.currentPoint.y < 0) {
                    return true;
                } else if (cp.currentPoint.y > mapHeight) {
                    return true;
                }
            }
        }

        return false; // No collision
    }
}
