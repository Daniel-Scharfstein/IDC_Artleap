package com.example.artleap_android;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artleap_android.openglHelper.PictureRenderer;
import com.example.artleap_android.utils.EditParametersSplitColors;

import java.io.File;
import java.util.Objects;

public class EditSplitColorsActivity extends AppCompatActivity {
    public static final int PINK = 0xFFF15ECF;
    public final String COLOR = "COLOR";
    public final String SPREADX = "SPREADX";
    public final String SPREADY = "SPREADY";

    PictureRenderer renderer;
    EditParametersSplitColors currentParameters = new EditParametersSplitColors(0.0, 0.5, 0.0);
    EditParametersSplitColors lastSavedParameters = new EditParametersSplitColors(0.0, 0.0, 0.0);
    Button colorButton;
    Button spreadXButton;
    Button spreadYButton;
    ImageButton xBox;
    ImageButton checkBox;
    SeekBar colorSeekBar;
    SeekBar spreadXSeekBar;
    SeekBar spreadYSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_split_colors_page);
        setActionBar();

        initializedAllButtonsAndSkBars();

        showSeekBar();
        clickOnBox();
        openImageWithOpenGl();
    }

    public void initializedAllButtonsAndSkBars() {
        colorSeekBar = findViewById(R.id.colorSkBar);
        colorSeekBar.setProgressTintList(ColorStateList.valueOf(PINK));
        colorSeekBar.setVisibility(View.GONE);
        spreadXSeekBar = findViewById(R.id.spreadXSkBar);
        spreadXSeekBar.setProgressTintList(ColorStateList.valueOf(PINK));
        spreadXSeekBar.setVisibility(View.GONE);
        spreadYSeekBar = findViewById(R.id.spreadYSkBar);
        spreadYSeekBar.setProgressTintList(ColorStateList.valueOf(PINK));
        spreadYSeekBar.setVisibility(View.GONE);

        colorButton = findViewById(R.id.colorButton);
        spreadXButton = findViewById(R.id.spreadXButton);
        spreadYButton = findViewById(R.id.spreadYButton);

        xBox = findViewById(R.id.xBox);
        xBox.setVisibility(View.GONE);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);

    }

    public void clickOnBox() {
        xBox.setOnClickListener(ib -> {
            hideSeekBars();
            currentParameters.setColor(lastSavedParameters.getColor());
            currentParameters.setSpreadX(lastSavedParameters.getSpreadX());
            currentParameters.setSpreadX(lastSavedParameters.getSpreadY());
            colorSeekBar.setProgress((int) (lastSavedParameters.getColor()));
            spreadXSeekBar.setProgress((int) (lastSavedParameters.getSpreadX()));
            spreadYSeekBar.setProgress((int) (lastSavedParameters.getSpreadY()));

        });
        checkBox.setOnClickListener(ib -> {
            hideSeekBars();
            lastSavedParameters.setColor(currentParameters.getColor());
            lastSavedParameters.setSpreadX(currentParameters.getSpreadX());
            lastSavedParameters.setSpreadY(currentParameters.getSpreadY());
        });
    }

    //Prevent from going to processing layout
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void hideSeekBars() {
        colorSeekBar.setVisibility(View.GONE);
        spreadXSeekBar.setVisibility(View.GONE);
        spreadYSeekBar.setVisibility(View.GONE);
        xBox.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        colorButton.setVisibility(View.VISIBLE);
        spreadXButton.setVisibility(View.VISIBLE);
        spreadYButton.setVisibility(View.VISIBLE);
    }

    public void showSeekBars(String buttonName) {
        spreadXButton.setVisibility(!buttonName.equals(SPREADX) ? View.GONE : View.VISIBLE);
        spreadYButton.setVisibility(!buttonName.equals(SPREADY) ? View.GONE : View.VISIBLE);
        colorButton.setVisibility(!buttonName.equals(COLOR) ? View.GONE : View.VISIBLE);
        colorSeekBar.setVisibility(buttonName.equals(COLOR) ? View.VISIBLE : View.GONE);
        spreadXSeekBar.setVisibility(buttonName.equals(SPREADX) ? View.VISIBLE : View.GONE);
        spreadYSeekBar.setVisibility(buttonName.equals(SPREADY) ? View.VISIBLE : View.GONE);
        xBox.setVisibility(View.VISIBLE);
        checkBox.setVisibility(View.VISIBLE);
    }

    public void showSeekBar() {
        int width = 1048;
        int height = 1015;
        colorButton.setOnClickListener(ib -> {
            showSeekBars(COLOR);
            colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    currentParameters.setColor(seekBar.getProgress());
                    renderer.currentParameters.setColor(currentParameters.getColor()/100);
                    renderer.onSurfaceChanged(null, width, height);
                    renderer.onDrawFrame(null);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        });
        spreadXButton.setOnClickListener(ib -> {
            showSeekBars(SPREADX);
            spreadXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    currentParameters.setSpreadX(seekBar.getProgress());
                    renderer.currentParameters.setSpreadX(currentParameters.getSpreadX()/100);
                    renderer.onSurfaceChanged(null, width, height);
                    renderer.onDrawFrame(null);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        });
        spreadYButton.setOnClickListener(ib -> {
            showSeekBars(SPREADY);
            spreadYSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    currentParameters.setSpreadY(seekBar.getProgress());
                    renderer.currentParameters.setSpreadY(currentParameters.getSpreadY()/100);
                    renderer.onSurfaceChanged(null, width, height);
                    renderer.onDrawFrame(null);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        });

    }

    public void setActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.edit_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openShare(MenuItem item) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        String pathOfBmp = MediaStore.Images.Media.insertImage(getContentResolver(), renderer.savedPicture, "title", null);
        Uri bmpUri = Uri.parse(pathOfBmp);
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
            GLSurfaceView gLView = this.findViewById(R.id.check);
            gLView.setEGLContextClientVersion(2);
            renderer = new PictureRenderer(getApplicationContext());
            renderer.pic = picture;
            renderer.currentParameters.setSpreadX(0.5f);
            gLView.setRenderer(renderer);
        }
    }
}