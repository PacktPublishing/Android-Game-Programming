package com.gamecodeschool.c11asteroids;

import android.graphics.PointF;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setRotateM;
import static android.opengl.Matrix.translateM;
import static com.gamecodeschool.c11asteroids.GLManager.*;

public class GameObject {

    boolean isActive;

    public enum Type {SHIP, ASTEROID, BORDER, BULLET, STAR}

    private Type type;

    private static int glProgram =-1;

    // How many vertices does it take to make
    // this particular game object?
    private int numElements;
    private int numVertices;

    // To hold the coordinates of the vertices that
    // define our GameObject model
    private float[] modelVertices;


    // Which way is the object moving and how fast?
    private float xVelocity = 0f;
    private float yVelocity = 0f;
    private float speed = 0;
    private float maxSpeed = 200;

    // Where is the object centre in the game world?
    private PointF worldLocation = new PointF();

    // This will hold our vertex data that is
    // passed into the openGL glProgram
    // OPenGL likes FloatBuffer
    private FloatBuffer vertices;

    // For translating each point from the model (ship, asteroid etc)
    // to its game world coordinates
    private final float[] modelMatrix = new float[16];

    // Some more matrices for Open GL transformations
    float[] viewportModelMatrix = new float[16];
    float[] rotateViewportModelMatrix = new float[16];

    // Where is the GameObject facing?
    private float facingAngle = 90f;

    // How fast is it rotating?
    private float rotationRate = 0f;

    // Which direction is it heading?
    private float travellingAngle = 0f;

    // How long and wide is the GameObject?
    private float length;
    private float width;


    public GameObject(){
        // Only compile shaders once
        if (glProgram == -1){
            setGLProgram();

            // tell OpenGl to use the glProgram
            glUseProgram(glProgram);

            // Now we have a glProgram we need the locations
            // of our three GLSL variables.
            // We will use these when we call draw on the object.
            uMatrixLocation = glGetUniformLocation(glProgram, U_MATRIX);
            aPositionLocation = glGetAttribLocation(glProgram, A_POSITION);
            uColorLocation = glGetUniformLocation(glProgram, U_COLOR);
        }

        // Set the object as active
        isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setGLProgram(){
        glProgram = GLManager.getGLProgram();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type t) {
        this.type = t;
    }

    public void setSize(float w, float l){
        width = w;
        length = l;

    }

    public PointF getWorldLocation() {
        return worldLocation;
    }

    public void setWorldLocation(float x, float y) {
        this.worldLocation.x = x;
        this.worldLocation.y = y;
    }

    public void setVertices(float[] objectVertices){

        modelVertices = new float[objectVertices.length];
        modelVertices = objectVertices;

        //Log.e("objectVertices[0]",""+objectVertices[0]);
        //Log.e("modelVertices[0]",""+modelVertices[0]);

        // Store how many vertices and elements there is for future use
        numElements = modelVertices.length;

        //Log.e("numElements",""+numElements);
        numVertices = numElements/ELEMENTS_PER_VERTEX;

        // Initialize the vertices ByteBuffer object based on the
        // number of vertices in the ship design and the number of
        // bytes there are in the float type
        vertices = ByteBuffer.allocateDirect(
                numElements
                        * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Add the ship into the ByteBuffer object
        vertices.put(modelVertices);

    }


    public void draw(float[] viewportMatrix){

        // tell OpenGl to use the glProgram
        glUseProgram(glProgram);

        // Set vertices to the first byte
        vertices.position(0);

        glVertexAttribPointer(
                aPositionLocation,
                COMPONENTS_PER_VERTEX,
                GL_FLOAT,
                false,
                STRIDE,
                vertices);

        glEnableVertexAttribArray(aPositionLocation);

        // Translate model coordinates into world coordinates
        // Make an identity matrix to base our future calculations on
        // Or we will get very strange results
        setIdentityM(modelMatrix, 0);
        // Make a translation matrix
        /*
            Parameters
            m	matrix
            mOffset	index into m where the matrix starts
            x	translation factor x
            y	translation factor y
            z	translation factor z
        */
        translateM(modelMatrix, 0, worldLocation.x, worldLocation.y, 0);

        // Combine the model with the viewport
        // into a new matrix
        multiplyMM(viewportModelMatrix, 0, viewportMatrix, 0, modelMatrix, 0);

        /*
            Now rotate the model - just the ship model

            Parameters
            rm	returns the result
            rmOffset	index into rm where the result matrix starts
            a	angle to rotate in degrees
            x	X axis component
            y	Y axis component
            z	Z axis component
        */
        setRotateM(modelMatrix, 0, facingAngle, 0, 0, 1.0f);

        // And multiply the rotation matrix into the model-viewport matrix
        multiplyMM(rotateViewportModelMatrix, 0, viewportModelMatrix, 0, modelMatrix, 0);

        // Give the matrix to OpenGL
        //glUniformMatrix4fv(uMatrixLocation, 1, false, viewportMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, rotateViewportModelMatrix, 0);
        // Assign a color to the fragment shader
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);

        // Draw the point, lines or triangle
        switch (type){
            case SHIP:
                glDrawArrays(GL_TRIANGLES, 0, numVertices);
                break;

            case ASTEROID:
                glDrawArrays(GL_LINES, 0, numVertices);
                break;

            case BORDER:
                glDrawArrays(GL_LINES, 0, numVertices);
                break;

            case STAR:
                glDrawArrays(GL_POINTS, 0, numVertices);
                break;

            case BULLET:
                glDrawArrays(GL_POINTS, 0, numVertices);
                break;
        }

    }

    /////////////////////////////////////////////////
    public void setRotationRate(float rotationRate) {
        this.rotationRate = rotationRate;
    }

    public float getTravellingAngle() {
        return travellingAngle;
    }

    public void setTravellingAngle(float travellingAngle) {
        this.travellingAngle = travellingAngle;
    }

    public float getFacingAngle() {
        return facingAngle;
    }

    public void setFacingAngle(float facingAngle) {
        this.facingAngle = facingAngle;
    }


    void move(float fps){
        if(xVelocity != 0) {
            worldLocation.x += xVelocity / fps;
        }

        if(yVelocity != 0) {
            worldLocation.y += yVelocity / fps;
        }

        // Rotate
        if(rotationRate != 0) {
            facingAngle = facingAngle + rotationRate / fps;
        }


    }

    public float getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public float getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    ////////////////////////////////////////////////

}
