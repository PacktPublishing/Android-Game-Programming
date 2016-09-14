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
import static android.opengl.Matrix.orthoM;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.gamecodeschool.c11asteroids.GLManager.A_POSITION;
import static com.gamecodeschool.c11asteroids.GLManager.COMPONENTS_PER_VERTEX;
import static com.gamecodeschool.c11asteroids.GLManager.FLOAT_SIZE;
import static com.gamecodeschool.c11asteroids.GLManager.STRIDE;
import static com.gamecodeschool.c11asteroids.GLManager.U_COLOR;
import static com.gamecodeschool.c11asteroids.GLManager.U_MATRIX;


public class TallyIcon {

    // For button coordinate
    // into a GL space coordinate (-1,-1 to 1,1)
    // for drawing on the screen
    private final float[] viewportMatrix = new float[16];

    // A handle to the GL glProgram -
    // the compiled and linked shaders
    private static int glProgram;

    // Each of the above constants also has a matching int
    // which will represent its location in the open GL glProgram
    // In GameButton they are declared as local variables

    // How many vertices does it take to make
    // our button
    private int numVertices;

    // This will hold our vertex data that is
    // passed into openGL glProgram
    //private final FloatBuffer vertices;
    private FloatBuffer vertices;

    public TallyIcon(GameManager gm, int nthIcon){

        // The HUD needs its own viewport
        // notice we set the screen height in pixels as the
        // starting y coordinates because
        // OpenGL is upside down world :-)
        orthoM(viewportMatrix, 0, 0, gm.screenWidth, gm.screenHeight, 0, 0f, 1f);

        float padding = gm.screenWidth / 160;
        float iconHeight = gm.screenHeight / 15;
        float iconWidth = 1; // square icons
        float startX = 10 + (padding + iconWidth)* nthIcon;
        float startY = iconHeight * 2 + padding;

        PointF p1 = new PointF();
        p1.x = startX;
        p1.y = startY;

        PointF p2 = new PointF();
        p2.x = startX;
        p2.y = startY - iconHeight;

        // Add the four points to an array of vertices
        // This time, because we don't need to animate the border
        // we can just declare the world space coordinates, the
        // same as above.
        float[] modelVertices = new float[]{
                // A line from point 1 to point 2
                p1.x, p1.y, 0,
                p2.x, p2.y, 0,


        };


        // Store how many vertices and elements there is for future use
        final int ELEMENTS_PER_VERTEX = 3;// x,y,z
        int numElements = modelVertices.length;
        numVertices = numElements/ELEMENTS_PER_VERTEX;

        // Initialize the vertices ByteBuffer object based on the
        // number of vertices in the button and the number of
        // bytes there are in the float type
        vertices = ByteBuffer.allocateDirect(
                numElements
                        * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Add the button into the ByteBuffer object
        vertices.put(modelVertices);

        glProgram = GLManager.getGLProgram();
    }

    public void draw(){

        // And tell OpenGl to use the glProgram
        glUseProgram(glProgram);

        // Now we have a glProgram we need the locations
        // of our three GLSL variables
        int uMatrixLocation = glGetUniformLocation(glProgram, U_MATRIX);
        int aPositionLocation = glGetAttribLocation(glProgram, A_POSITION);
        int uColorLocation = glGetUniformLocation(glProgram, U_COLOR);

        vertices.position(0);

        glVertexAttribPointer(
                aPositionLocation,
                COMPONENTS_PER_VERTEX,
                GL_FLOAT,
                false,
                STRIDE,
                vertices);

        glEnableVertexAttribArray(aPositionLocation);

        // Just give the passed in matrix to OpenGL
        glUniformMatrix4fv(uMatrixLocation, 1, false, viewportMatrix, 0);
        // Assign a color to the fragment shader
        glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        // Draw the lines
        // start at the first element of the vertices array and read in all vertices
        glDrawArrays(GL_LINES, 0, numVertices);
    }






}
