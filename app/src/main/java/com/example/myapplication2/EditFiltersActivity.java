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

import com.example.myapplication2.openglHelper.PictureRenderer;
import com.example.myapplication2.utils.EditParameters;

import java.io.File;
import java.util.Objects;

public class EditFiltersActivity extends AppCompatActivity {
    public static final int PINK = 0xFFF15ECF;
    public final String COLOR = "COLOR";
    public final String ANGLE = "ANGLE";
    public final String SPREAD = "SPREAD";

    PictureRenderer renderer;
    EditParameters currentParameters = new EditParameters(0.0, 0.0);
    EditParameters lastSavedParameters = new EditParameters(0.0, 0.0);
    ImageButton xBox;
    ImageButton checkBox;
    SeekBar colorSeekBar;
    SeekBar angleSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_filters_page);
        setActionBar();

        initializedAllButtonsAndSkBars();
        Button filter1 = findViewById(R.id.filter1);
        filter1.setOnClickListener(ib->{
            xBox.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
        });
//        showSeekBar();
        clickOnBox();

        openImageWithOpenGl();
    }

    public void initializedAllButtonsAndSkBars() {
        colorSeekBar = findViewById(R.id.colorSkBar);
        colorSeekBar.setProgressTintList(ColorStateList.valueOf(PINK));
        colorSeekBar.setVisibility(View.GONE);
        angleSeekBar = findViewById(R.id.angleSkBar);
        angleSeekBar.setProgressTintList(ColorStateList.valueOf(PINK));
        angleSeekBar.setVisibility(View.GONE);

//        colorButton = findViewById(R.id.colorButton);
//        angleButton = findViewById(R.id.angleButton);
//        spreadButton = findViewById(R.id.spreadButton);

        xBox = findViewById(R.id.xBox);
        xBox.setVisibility(View.GONE);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);

    }

    public void clickOnBox() {
        xBox.setOnClickListener(ib -> {
            hideSeekBars();
            currentParameters.setColor(lastSavedParameters.getColor());
            System.out.println(lastSavedParameters.getAngle());
            currentParameters.setAngle(lastSavedParameters.getAngle());
            colorSeekBar.setProgress((int) (lastSavedParameters.getColor()));
            angleSeekBar.setProgress((int) (lastSavedParameters.getAngle()));
        });
        checkBox.setOnClickListener(ib -> {
            hideSeekBars();
            lastSavedParameters.setColor(currentParameters.getColor());
            lastSavedParameters.setAngle(currentParameters.getAngle());
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
        angleSeekBar.setVisibility(View.GONE);
        xBox.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
//        colorButton.setVisibility(View.VISIBLE);
//        angleButton.setVisibility(View.VISIBLE);
//        spreadButton.setVisibility(View.VISIBLE);
    }

    public void showSeekBars(String buttonName) {
//        angleButton.setVisibility(!buttonName.equals(ANGLE) ? View.GONE : View.VISIBLE);
//        spreadButton.setVisibility(!buttonName.equals(SPREAD) ? View.GONE : View.VISIBLE);
//        colorButton.setVisibility(!buttonName.equals(COLOR) ? View.GONE : View.VISIBLE);
        colorSeekBar.setVisibility(buttonName.equals(COLOR) ? View.VISIBLE : View.GONE);
        angleSeekBar.setVisibility(buttonName.equals(ANGLE) ? View.VISIBLE : View.GONE);
        xBox.setVisibility(View.VISIBLE);
        checkBox.setVisibility(View.VISIBLE);
    }

//    public void showSeekBar() {
//        int width = 1048;
//        int height = 1015;
//        colorButton.setOnClickListener(ib -> {
//            showSeekBars(COLOR);
//            colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
////                    currentParameters.setColor(seekBar.getProgress());
////                    renderer.currentParameters.setColor(currentParameters.getColor()/100);
//                    renderer.onSurfaceChanged(null, width, height);
//                    renderer.onDrawFrame(null);
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
//        angleButton.setOnClickListener(ib -> {
//            showSeekBars(ANGLE);
//            angleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
////                    currentParameters.setAngle(seekBar.getProgress());
////                    renderer.currentParameters.setAngle(currentParameters.getAngle()/100);
//                    renderer.onSurfaceChanged(null, width, height);
//                    renderer.onDrawFrame(null);
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
//    }

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
            GLSurfaceView gLView =  this.findViewById(R.id.check);
            gLView.setEGLContextClientVersion(2);
            renderer = new PictureRenderer(getApplicationContext());
            renderer.pic = picture;
            gLView.setRenderer(renderer);
        }
    }
}