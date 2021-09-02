package com.example.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;


public class OpenGLES20Activity extends Activity {

    private GLSurfaceView gLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        Intent myIntent = getIntent();
//        byte[] byteArray = myIntent.getByteArrayExtra("picture"); // will return "FirstKeyValue"
//        Bitmap pic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }


    class MyGLSurfaceView extends GLSurfaceView {

        private final MyGLRenderer renderer;

        public MyGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);
            renderer = new MyGLRenderer();



//            renderer.setPicForDraw(pic);
            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(renderer);

        }
    }
}



