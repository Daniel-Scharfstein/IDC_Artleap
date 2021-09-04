package com.example.myapplication2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;

public class EditImageActivity extends AppCompatActivity {
    public static final int PINK = 0xFFF15ECF;

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

        ImageButton button = findViewById(R.id.shareButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
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
        getSupportActionBar().setCustomView(R.layout.action_bar);
    }
}