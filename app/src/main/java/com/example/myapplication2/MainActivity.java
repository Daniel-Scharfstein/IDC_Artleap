package com.example.myapplication2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ProgressBar splashProgress;
    int SPLASH_TIME = 2000; //This is 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();

        getWindow().getDecorView().setBackgroundColor(Color.DKGRAY);

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mySuperIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(mySuperIntent);
                finish();
            }
        }, SPLASH_TIME);



    }

    //Method to run progress bar for 5 seconds
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(2000)
                .start();
    }
}
