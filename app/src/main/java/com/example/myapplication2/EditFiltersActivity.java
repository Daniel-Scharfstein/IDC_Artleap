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
import com.example.myapplication2.openglHelper.PictureRenderer;
import com.example.myapplication2.utils.EditParameters;

import java.io.File;
import java.util.Objects;

public class EditFiltersActivity extends AppCompatActivity {
    public static final int PINK = 0xFFF15ECF;
    public final String COLOR = "COLOR";
    public final String ANGLE = "ANGLE";
    public final String SPREAD = "SPREAD";

    FilterRenderer renderer;
    EditParameters currentParameters = new EditParameters(0.0, 0.0, 0.0);
    EditParameters lastSavedParameters = new EditParameters(0.0, 0.0, 0.0);
    ImageButton xBox;
    ImageButton checkBox;
    Button colorButton;
    SeekBar colorSeekBar;

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
        showSeekBar();
        clickOnBox();

        openImageWithOpenGl();
    }

    public void initializedAllButtonsAndSkBars() {
        colorSeekBar = findViewById(R.id.colorSkBar);
        colorSeekBar.setProgressTintList(ColorStateList.valueOf(PINK));
        colorSeekBar.setVisibility(View.GONE);

        colorButton = findViewById(R.id.filter1);
        xBox = findViewById(R.id.xBox);
        xBox.setVisibility(View.GONE);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);

    }

    public void clickOnBox() {
        xBox.setOnClickListener(ib -> {
            hideSeekBars();
            currentParameters.setColor(lastSavedParameters.getColor());
            colorSeekBar.setProgress((int) (lastSavedParameters.getColor()));
        });
        checkBox.setOnClickListener(ib -> {
            hideSeekBars();
            lastSavedParameters.setColor(currentParameters.getColor());
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
    }

    public void showSeekBars(String buttonName) {
        colorSeekBar.setVisibility(buttonName.equals(COLOR) ? View.VISIBLE : View.GONE);
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
            GLSurfaceView gLView =  this.findViewById(R.id.check);
            gLView.setEGLContextClientVersion(2);
            renderer = new FilterRenderer(getApplicationContext());
            renderer.pic = picture;
            renderer.currentParameters.setColor(0.0f);
            gLView.setRenderer(renderer);
        }
    }
}