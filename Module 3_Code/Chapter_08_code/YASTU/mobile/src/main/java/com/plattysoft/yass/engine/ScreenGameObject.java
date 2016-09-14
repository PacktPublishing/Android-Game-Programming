package com.plattysoft.yass.engine;

import android.graphics.Rect;

/**
 * Created by Raul Portales on 01/04/15.
 */
public abstract class ScreenGameObject extends GameObject {

    protected double mX;
    protected double mY;

    protected int mHeight;
    protected int mWidth;

    public double mRadius;

    public BodyType mBodyType;

    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
    }

    public Rect mBoundingRect = new Rect(-1, -1, -1, -1);

    public void onPostUpdate(GameEngine gameEngine) {
        mBoundingRect.set(
                (int) mX,
                (int) mY,
                (int) mX + mWidth,
                (int) mY + mHeight);
    }

    public boolean checkCollision(ScreenGameObject otherObject) {
        if (mBodyType == BodyType.Circular && otherObject.mBodyType == BodyType.Circular) {
            return checkCircularCollision(otherObject);
        }
        else if (mBodyType == BodyType.Rectangular && otherObject.mBodyType == BodyType.Rectangular) {
            return checkRectangularCollision(otherObject);
        }
        else {
            return checkMixedCollision(otherObject);
        }
    }

    private boolean checkMixedCollision(ScreenGameObject other) {
        ScreenGameObject circularSprite;
        ScreenGameObject rectangularSprite;
        if (mBodyType == BodyType.Rectangular) {
            circularSprite = this;
            rectangularSprite = other;
        }
        else {
            circularSprite = other;
            rectangularSprite = this;
        }

        double circleCenterX = circularSprite.mX + circularSprite.mWidth /2;
        double positionXToCheck = circleCenterX;
        if (circleCenterX < rectangularSprite.mX) {
            positionXToCheck = rectangularSprite.mX;
        }
        else if (circleCenterX > rectangularSprite.mX + rectangularSprite.mWidth) {
            positionXToCheck = rectangularSprite.mX + rectangularSprite.mWidth;
        }
        double distanceX = circleCenterX - positionXToCheck;

        double circleCenterY = circularSprite.mY + circularSprite.mHeight /2;
        double positionYToCheck = circleCenterY;
        if (circleCenterY < rectangularSprite.mY) {
            positionYToCheck = rectangularSprite.mY;
        }
        else if (circleCenterY > rectangularSprite.mY + rectangularSprite.mHeight) {
            positionYToCheck = rectangularSprite.mY + rectangularSprite.mHeight;
        }
        double distanceY = circleCenterY - positionYToCheck;

        double squareDistance = distanceX*distanceX + distanceY*distanceY;
        if (squareDistance <= circularSprite.mRadius*circularSprite.mRadius) {
            // They are overlapping
            return true;
        }
        return false;
    }

    private boolean checkRectangularCollision(ScreenGameObject other) {
        return Rect.intersects(mBoundingRect, other.mBoundingRect);
    }

    private boolean checkCircularCollision(ScreenGameObject other) {
        double distanceX = (mX + mWidth /2) - (other.mX + other.mWidth /2);
        double distanceY = (mY + mHeight /2) - (other.mY + other.mHeight /2);
        double squareDistance = distanceX*distanceX + distanceY*distanceY;
        double collisionDistance = (mRadius + other.mRadius);
        return squareDistance <= collisionDistance*collisionDistance;
    }

}
