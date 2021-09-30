package com.example.artleap_android;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class WelcomeSplashScreenActivity extends AppCompatActivity {

    ProgressBar splashProgress;
    int SPLASH_TIME = 2000; //This is 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_welcome_page);
        setActionBar();

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mySuperIntent = new Intent(WelcomeSplashScreenActivity.this, HomeActivity.class);
                startActivity(mySuperIntent);
                finish();
            }
        }, SPLASH_TIME);


    }

    //Method to run progress bar for 2 seconds
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(2000)
                .start();
    }

    public void setActionBar() {
        // TODO Auto-generated method stub
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setCustomView(R.layout.action_bar);
    }
}
