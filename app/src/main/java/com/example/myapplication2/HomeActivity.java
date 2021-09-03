package com.example.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    ImageView imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        addListenerOnTemplateImage();
        setActionBar();


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

    public void setActionBar() {
        // TODO Auto-generated method stub
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

    }
}