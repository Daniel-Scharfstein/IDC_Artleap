package com.example.myapplication2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.openglHelper.PictureRenderer;
import com.example.myapplication2.utils.EditParameters;

import java.io.File;
import java.util.Objects;

public class EditImageActivity extends AppCompatActivity {
    public static final int PINK = 0xFFF15ECF;
    MenuItem menuItem;
    private GLSurfaceView gLView;
    public EditParameters editParameters = new EditParameters(0.0, 0.0);


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
        ImageButton xBox = findViewById(R.id.xBox);
        ImageButton checkBox = findViewById(R.id.checkBox);

        xBox.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);

        skbar.setVisibility(View.GONE);
        showSeekBar(skbar, colorButton, angleButton, spreadButton, xBox, checkBox);

        xBox.setOnClickListener(ib -> {
            backToEditClick(skbar, colorButton, angleButton, spreadButton, xBox, checkBox);
        });

        checkBox.setOnClickListener(ib -> {
            backToEditClick(skbar, colorButton, angleButton, spreadButton, xBox, checkBox);
        });

        openImageWithOpenGl();


    }

    public void backToEditClick(SeekBar skbar, ImageButton colorButton, ImageButton angleButton, ImageButton spreadButton, ImageButton xBox, ImageButton checkBox) {
        skbar.setVisibility(View.GONE);
        xBox.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        colorButton.setVisibility(View.VISIBLE);
        angleButton.setVisibility(View.VISIBLE);
        spreadButton.setVisibility(View.VISIBLE);
        System.out.println(editParameters.getColor() + "  " + editParameters.getAngle());
    }

    public void showSeekBar(SeekBar skbar, ImageButton colorButton, ImageButton angleButton, ImageButton spreadButton, ImageButton xBox, ImageButton checkBox) {
        colorButton.setOnClickListener(ib -> {
            angleButton.setVisibility(View.GONE);
            spreadButton.setVisibility(View.GONE);
            skbar.setVisibility(View.VISIBLE);
            xBox.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);

            skbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    System.out.println("in color");
                    editParameters.setColor(seekBar.getProgress());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            skbar.setProgress(0);
        });
        angleButton.setOnClickListener(ib -> {
            //For changing visibility
            colorButton.setVisibility(View.GONE);
            spreadButton.setVisibility(View.GONE);
            skbar.setVisibility(View.VISIBLE);
            xBox.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
            skbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    System.out.println("in angle");
                    editParameters.setAngle(seekBar.getProgress());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

            });
            skbar.setProgress(0);
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
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
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
            gLView = (GLSurfaceView) this.findViewById(R.id.check);
            gLView.setEGLContextClientVersion(2);
            PictureRenderer renderer = new PictureRenderer(getApplicationContext());
            renderer.pic = picture;
            gLView.setRenderer(renderer);
        }
    }
}