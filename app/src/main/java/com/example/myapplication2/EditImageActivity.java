package com.example.myapplication2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication2.openglHelper.PictureRenderer;

import java.io.File;
import java.util.Objects;

public class EditImageActivity extends AppCompatActivity {
    public static final int PINK = 0xFFF15ECF;
    MenuItem menuItem;
    private GLSurfaceView gLView;
    PictureRenderer renderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_photo_page);
        setActionBar();

        SeekBar skbar = findViewById(R.id.seekBar);
        skbar.setProgressTintList(ColorStateList.valueOf(PINK));

        ImageButton colorButton = findViewById(R.id.colorButton);
        ImageButton angleButton = findViewById(R.id.angleButton);
        ImageButton spreadButton = findViewById(R.id.spreadButton);

        skbar.setVisibility(View.GONE);
        showSeekBar(skbar, colorButton, angleButton, spreadButton);

        ConstraintLayout rlayout = (ConstraintLayout) findViewById(R.id.allPage);
        rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skbar.setVisibility(View.GONE);
                skbar.setProgress(0);
                colorButton.setVisibility(View.VISIBLE);
                angleButton.setVisibility(View.VISIBLE);
                spreadButton.setVisibility(View.VISIBLE);
            }

        });

        openImageWithOpenGl();


    }

    public void showSeekBar(SeekBar skbar, ImageButton colorButton, ImageButton angleButton, ImageButton spreadButton) {
        colorButton.setOnClickListener(ib -> {
            //For changing visibility
            angleButton.setVisibility(View.GONE);
            spreadButton.setVisibility(View.GONE);
            skbar.setVisibility(View.VISIBLE);
        });
        angleButton.setOnClickListener(ib -> {
            //For changing visibility
            colorButton.setVisibility(View.GONE);
            spreadButton.setVisibility(View.GONE);
            skbar.setVisibility(View.VISIBLE);
        });
        spreadButton.setOnClickListener(ib -> {
            //For changing visibility
            colorButton.setVisibility(View.GONE);
            angleButton.setVisibility(View.GONE);
            skbar.setVisibility(View.VISIBLE);
        });
    }

    public void setActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.actionbar1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openShare(MenuItem item) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), renderer.savedPicture  ,"title", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        sharingIntent.setType("image/png");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void openHomePage(MenuItem item) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void openImageWithOpenGl() {

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        String filePath = getIntent().getStringExtra("path");
        File file = new File(filePath);
        Bitmap picture = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (supportsEs2) {
            setContentView(R.layout.edit_photo_page);
            gLView = (GLSurfaceView) this.findViewById(R.id.check);
            gLView.setEGLContextClientVersion(2);
            renderer = new PictureRenderer(getApplicationContext());
            renderer.pic = picture;
            gLView.setRenderer(renderer);
        }
    }
}