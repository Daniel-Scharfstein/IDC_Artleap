package com.example.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    ImageView imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        addListenerOnTemplateImage();


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setBackgroundColor(Color.DKGRAY);

    }

    public void addListenerOnTemplateImage() {

        imageButton = findViewById(R.id.imageView13);
        imageButton.setOnClickListener(view -> openLoadingPage());
    }
    public void openLoadingPage()
    {
        Intent intent = new Intent(this,UploadingImgActivity.class);
        startActivity(intent);
    }
}