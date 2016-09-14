package com.gamecodeschool.c10asteroids;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

public class GLManager {

    // Some constants to help count the number of bytes between
    // elements of our vertex data arrays
    public static final int COMPONENTS_PER_VERTEX = 3;
    public  static final int FLOAT_SIZE = 4;
    public static final int STRIDE =
            (COMPONENTS_PER_VERTEX)
                    * FLOAT_SIZE;

    public static final int ELEMENTS_PER_VERTEX = 3;// x,y,z

    // Some constants to represent GLSL types in our shaders
    public static final String U_MATRIX = "u_Matrix";
    public static final String A_POSITION = "a_Position";
    public static final String U_COLOR = "u_Color";

    // Each of the above constants also has a matching int
    // which will represent its location in the open GL glProgram
    public static int uMatrixLocation;
    public static int aPositionLocation;
    public static int uColorLocation;

    // A very simple vertexShader glProgram
    // that we can define with a String
    private static String vertexShader =
            "uniform mat4 u_Matrix;" +
                    "attribute vec4 a_Position;" +

                    "void main()" +
                    "{" +
                        "gl_Position = u_Matrix * a_Position;" +
                        "gl_PointSize = 3.0;"+
                    "}";

    // A very simple fragment Shader glProgram
    // that we can define with a String
    private static String fragmentShader =
            "precision mediump float;" +
                    "uniform vec4 u_Color;" +

                    "void main()" +
                    "{" +
                        "gl_FragColor = u_Color;" +
                    "}";

    // A handle to the GL glProgram
    public static int program;

    public static int getGLProgram(){
        return program;
    }

    public static int buildProgram(){
        // Compile and link our shaders into a GL glProgram object
        return linkProgram(compileVertexShader(),compileFragmentShader());
    }

    private static int compileVertexShader() {
        return compileShader(GL_VERTEX_SHADER, vertexShader);
    }

    private static int compileFragmentShader() {
        return compileShader(GL_FRAGMENT_SHADER, fragmentShader);
    }

    private static int compileShader(int type, String shaderCode) {

        // Create a shader object and store its ID
        final int shader = glCreateShader(type);

        // Pass in the code then compile the shader
        glShaderSource(shader, shaderCode);
        glCompileShader(shader);

        return shader;
    }

    private static int linkProgram(int vertexShader, int fragmentShader) {

        // A handle to the GL glProgram -
        // the compiled and linked shaders
        program = glCreateProgram();

        // Attach the vertex shader to the glProgram.
        glAttachShader(program, vertexShader);

        // Attach the fragment shader to the glProgram.
        glAttachShader(program, fragmentShader);

        // Link the two shaders together into a glProgram.
        glLinkProgram(program);

        return program;
    }
}