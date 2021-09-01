package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnTemplateImage();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    public void addListenerOnTemplateImage() {

        imageButton = findViewById(R.id.imageView4);
        imageButton.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this,
                    "ImageButton is clicked!", Toast.LENGTH_SHORT).show();
            openLoadingPage();
        });
    }
    public void openLoadingPage()
    {
        Intent intent = new Intent(this,LoadingScreenActivity.class);
        startActivity(intent);
    }
}