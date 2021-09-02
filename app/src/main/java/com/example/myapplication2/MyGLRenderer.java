//package com.example.myapplication2;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//
//import android.graphics.Bitmap;
//import android.opengl.GLES20;
//import android.opengl.GLSurfaceView;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//
//public class MyGLRenderer implements GLSurfaceView.Renderer {
//
//    private Triangle triangle;
//    private Square square;
//    private Bitmap picture;
//
//
//    public static int loadShader(int type, String shaderCode){
//
//        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
//        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
//        int shader = GLES20.glCreateShader(type);
//
//        // add the source code to the shader and compile it
//        GLES20.glShaderSource(shader, shaderCode);
//        GLES20.glCompileShader(shader);
//
//        return shader;
//    }
//
//    public void setPicture(Bitmap picture){
//        this.picture = picture;
//    }
//
//    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
//        // Set the background frame color
//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//        square.loadGLTexture(picture);
//
//        GLES20.glEnable(GLES20.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
//        GLES20.glClearDepthf(1.0f); 					//Depth Buffer Setup
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST); 			//Enables Depth Testing
//        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
//
//
//    }
//
//    public void onDrawFrame(GL10 unused) {
//        // Redraw background color
//        square = new Square();
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        square.draw();
//    }
//
//    public void onSurfaceChanged(GL10 unused, int width, int height) {
//        GLES20.glViewport(0, 0, width, height);
//    }
//
//
//}
//
