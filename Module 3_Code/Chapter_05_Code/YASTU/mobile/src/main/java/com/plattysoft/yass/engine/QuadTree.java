package com.plattysoft.yass.engine;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Raul Portales on 31/03/15.
 */
public class QuadTree {

    private static final int MAX_QUADTREES = 12;
    private static int MAX_OBJECTS_TO_CHECK = 8;

    private List<ScreenGameObject> mGameObjects = new ArrayList<ScreenGameObject>();
    private Rect mArea = new Rect();

    private Rect mTmpRect = new Rect();

    private QuadTree[] mChildren = new QuadTree[4];

    private static List<QuadTree> sQuadTreePool = new ArrayList<QuadTree>();

    public static void init() {
        sQuadTreePool.clear();
        for (int i = 0; i < MAX_QUADTREES; i++) {
            sQuadTreePool.add(new QuadTree());
        }
    }

    public void setArea(Rect area) {
        mArea.set(area);
    }

    public void checkObjects(List<ScreenGameObject> gameObjects) {
        mGameObjects.clear();
        int numObjects = gameObjects.size();
        for (int i = 0; i < numObjects; i++) {
            ScreenGameObject current = gameObjects.get(i);
            Rect boundingRect = current.mBoundingRect;
            if (Rect.intersects(boundingRect, mArea)) {
                mGameObjects.add(current);
            }
        }
    }

    public void checkCollisions(GameEngine gameEngine, List<Collision> detectedCollisions) {
        int numObjects = mGameObjects.size();
        if (numObjects > MAX_OBJECTS_TO_CHECK && sQuadTreePool.size() >= 4) {
            // Split this area in 4
            splitAndCheck(gameEngine, detectedCollisions);
        }
        else {
            for (int i = 0; i < numObjects; i++) {
                ScreenGameObject objectA = mGameObjects.get(i);
                for (int j = i + 1; j < numObjects; j++) {
                    ScreenGameObject objectB = mGameObjects.get(j);
                    if (objectA.checkCollision(objectB)) {
                        Collision c = Collision.init(objectA, objectB);
                        if (!hasBeenDetected(detectedCollisions, c)) {
                            detectedCollisions.add(c);
                            objectA.onCollision(gameEngine, objectB);
                            objectB.onCollision(gameEngine, objectA);
                        }
                    }
                }
            }
        }
    }

    private boolean hasBeenDetected(List<Collision> detectedCollisions, Collision c) {
        int numCollisions = detectedCollisions.size();
        for (int i=0; i<numCollisions; i++) {
            if (detectedCollisions.get(i).equals(c)) {
                return true;
            }
        }
        return false;
    }

    private void splitAndCheck(GameEngine gameEngine, List<Collision> detectedCollisions) {
        for (int i=0 ; i<4; i++) {
            mChildren[i] = sQuadTreePool.remove(0);
        }
        for (int i=0 ; i<4; i++) {
            mChildren[i].setArea(getArea(i));
            mChildren[i].checkObjects(mGameObjects);
            mChildren[i].checkCollisions(gameEngine, detectedCollisions);
            // Clear and return to the pool
            mChildren[i].mGameObjects.clear();
            sQuadTreePool.add(mChildren[i]);
        }
    }

    private Rect getArea(int area) {
        int startX = mArea.left;
        int startY = mArea.top;
        int width = mArea.width();
        int height = mArea.height();
        switch (area) {
            case 0:
                mTmpRect.set(startX, startY, startX + width / 2, startY + height / 2);
                break;
            case 1:
                mTmpRect.set(startX + width / 2, startY, startX + width, startY + height / 2);
                break;
            case 2:
                mTmpRect.set(startX, startY + height / 2, startX + width / 2, startY + height);
                break;
            case 3:
                mTmpRect.set(startX + width / 2, startY + height / 2, startX + width, startY + height);
                break;
        }
        return mTmpRect;
    }

    public void addGameObject(ScreenGameObject sgo) {
        mGameObjects.add(sgo);
    }

    public void removeGameObject(ScreenGameObject objectToRemove) {
        mGameObjects.remove(objectToRemove);
    }
}
