package com.example.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.Objects;

public class LoadingScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_splash);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        SeekBar skbar = findViewById(R.id.seekBar);
        skbar.setVisibility(View.GONE);
        Button btn =  findViewById(R.id.button);
        btn.setOnClickListener(ib -> {
            //For changing visibility
            btn.setVisibility(View.GONE);
            skbar.setVisibility(View.VISIBLE);
        });
        RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.allPage);
        rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skbar.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
            }

        });

    }

}