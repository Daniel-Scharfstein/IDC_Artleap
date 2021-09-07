package com.example.myapplication2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.myapplication2.openglHelper.PictureRenderer;

import java.io.File;

public class OpenGLES20Activity extends Activity {

    private GLSurfaceView gLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        String filePath = getIntent().getStringExtra("path");
        File file = new File(filePath);
        Bitmap picture = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (supportsEs2)
        {
            setContentView(R.layout.edit_photo_page);
            gLView = (GLSurfaceView) this.findViewById(R.id.check);
            gLView.setEGLContextClientVersion(2);
//            MyGLRenderer renderer = new MyGLRenderer(getApplicationContext());
            PictureRenderer renderer = new PictureRenderer(getApplicationContext());
            renderer.pic = picture;
            gLView.setRenderer(renderer);
        }
        else
        {
            return;
        }

    }




}