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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.openglHelper.FilterRenderer;
import com.example.myapplication2.utils.EditParametersFilters;

import java.io.File;
import java.util.Objects;

public class EditFiltersActivity extends AppCompatActivity {
    public static final int PINK = 0xFFF15ECF;
    public final String PURPLE = "PURPLE";
    public final String LIGHT_BLUE = "LIGHT BLUE";
    public final String RED = "RED";
    public final String YELLOW = "YELLOW";
    public final String DARK_GREEN = "DARK GREEN";
    public final String DARK_PINK = "PINK";

    FilterRenderer renderer;
//    EditParametersFilters currentParameters = new EditParametersFilters(LIGHT_BLUE, 50.0);
    EditParametersFilters lastSavedParameters = new EditParametersFilters(null, 0.5);
    ImageButton xBox;
    ImageButton checkBox;
    Button lightBlueColorButton;
    Button redColorButton;
    Button yellowColorButton;
    //    Button pinkColorButton;
//    Button purpleColorButton;
    Button darkGreenColorButton;

    SeekBar colorSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_filters_page);
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

        lightBlueColorButton = findViewById(R.id.lightBlue);
        redColorButton = findViewById(R.id.red);
        yellowColorButton = findViewById(R.id.yellow);
//        pinkColorButton = findViewById(R.id.pink);
//        purpleColorButton = findViewById(R.id.purple);
        darkGreenColorButton = findViewById(R.id.darkGreen);

        xBox = findViewById(R.id.xBox);
        xBox.setVisibility(View.GONE);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);

    }

    public void clickOnBox() {
        xBox.setOnClickListener(ib -> {
            hideSeekBars();
            renderer.currentParameters.setColor(lastSavedParameters.getColor());
            colorSeekBar.setProgress((int) (lastSavedParameters.getPercentage()*100));
            System.out.println(renderer.currentParameters.getColor() + "  " + renderer.currentParameters.getPercentage());
        });
        checkBox.setOnClickListener(ib -> {
            hideSeekBars();
            lastSavedParameters.setPercentage(renderer.currentParameters.getPercentage());
            lastSavedParameters.setColor(renderer.currentParameters.getColor());
            System.out.println(renderer.currentParameters.getColor() + "  " + renderer.currentParameters.getPercentage());
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
        xBox.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);

        lightBlueColorButton.setVisibility(View.VISIBLE);
        redColorButton.setVisibility(View.VISIBLE);
        yellowColorButton.setVisibility(View.VISIBLE);
        darkGreenColorButton.setVisibility(View.VISIBLE);
//        pinkColorButton.setVisibility(View.VISIBLE);
//        purpleColorButton.setVisibility(View.VISIBLE);
    }

    public void showSeekBars(String buttonName) {
        lightBlueColorButton.setVisibility(buttonName.equals(LIGHT_BLUE) ? View.VISIBLE : View.GONE);
        yellowColorButton.setVisibility(buttonName.equals(YELLOW) ? View.VISIBLE : View.GONE);
        redColorButton.setVisibility(buttonName.equals(RED) ? View.VISIBLE : View.GONE);
//        pinkColorButton.setVisibility(buttonName.equals(DARK_PINK) ? View.VISIBLE : View.GONE);
//        purpleColorButton.setVisibility(buttonName.equals(PURPLE) ? View.VISIBLE : View.GONE);
        darkGreenColorButton.setVisibility(buttonName.equals(DARK_GREEN) ? View.VISIBLE : View.GONE);

        colorSeekBar.setVisibility(View.VISIBLE);
        xBox.setVisibility(View.VISIBLE);
        checkBox.setVisibility(View.VISIBLE);
    }

    public void showSeekBar() {
        int width = 1048;
        int height = 1015;
        lightBlueColorButton.setOnClickListener(ib -> {
            showSeekBars(LIGHT_BLUE);
//            currentParameters.setColor(LIGHT_BLUE);
            renderer.currentParameters.setColor(LIGHT_BLUE);
            colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    renderer.currentParameters.setPercentage((float) seekBar.getProgress()/100);
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
//        purpleColorButton.setOnClickListener(ib -> {
//            showSeekBars(PURPLE);
//            currentParameters.setColor(PURPLE);
//            colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                    currentParameters.setPercentage(seekBar.getProgress());
////                    renderer.currentParameters.setColor(currentParameters.getColor()/100);
////                    renderer.onSurfaceChanged(null, width, height);
////                    renderer.onDrawFrame(null);
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                }
//            });
//        });
//        pinkColorButton.setOnClickListener(ib -> {
//            showSeekBars(DARK_PINK);
//            currentParameters.setColor(DARK_PINK);
//            colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                    currentParameters.setPercentage(seekBar.getProgress());
////                    renderer.currentParameters.setColor(currentParameters.getColor()/100);
////                    renderer.onSurfaceChanged(null, width, height);
////                    renderer.onDrawFrame(null);
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                }
//            });
//        });
        darkGreenColorButton.setOnClickListener(ib -> {
            showSeekBars(DARK_GREEN);
//            currentParameters.setColor(DARK_GREEN);
            renderer.currentParameters.setColor(DARK_GREEN);
            colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    renderer.currentParameters.setPercentage((float) seekBar.getProgress()/100);
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
        yellowColorButton.setOnClickListener(ib -> {
            showSeekBars(YELLOW);
//            currentParameters.setColor(YELLOW);
            renderer.currentParameters.setColor(YELLOW);
            colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    renderer.currentParameters.setPercentage((float) seekBar.getProgress()/100);
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
        redColorButton.setOnClickListener(ib -> {
            showSeekBars(RED);
//            currentParameters.setColor(RED);
            renderer.currentParameters.setColor(RED);
            colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    renderer.currentParameters.setPercentage((float) seekBar.getProgress()/100);
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
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
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
            renderer = new FilterRenderer(getApplicationContext());
            renderer.pic = picture;
            gLView.setRenderer(renderer);
        }
    }
}