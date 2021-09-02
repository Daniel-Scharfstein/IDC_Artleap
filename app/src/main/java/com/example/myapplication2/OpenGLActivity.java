package com.example.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

public class OpenGLActivity extends Activity {

    /** The OpenGL view */
    private GLSurfaceView glSurfaceView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myIntent = getIntent();
        String filePath = getIntent().getStringExtra("path");
        File file = new File(filePath);
        Bitmap picture = BitmapFactory.decodeFile(file.getAbsolutePath());


        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initiate the Open GL view and
        // create an instance with this activity
        glSurfaceView = new GLSurfaceView(this);


        // set our renderer to be the main renderer with
        // the current activity context
        GlRenderer renderer = new GlRenderer(this);
        renderer.picture = picture;
        glSurfaceView.setRenderer(renderer);
        setContentView(glSurfaceView);
    }

    /** Remember to resume the glSurface  */
    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    /** Also pause the glSurface  */
    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}