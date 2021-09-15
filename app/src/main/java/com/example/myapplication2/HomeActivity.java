package com.example.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    ImageView imageButton;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        addListenerOnTemplateImage();
        setActionBar();

    }

    public void addListenerOnTemplateImage() {

        imageButton = findViewById(R.id.imageView13);
        imageButton.setOnClickListener(arg0 -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });
    }

    public void openSegmentationPage(Bitmap pic){
        Intent intent = new Intent(this, SegmentationActivity.class);
        String filePath = tempFileImage(this, pic, "tempImage");
        intent.putExtra("path", filePath);
        startActivity(intent);
    }

    public static String tempFileImage(Context context, Bitmap bitmap, String name) {

        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            cursor.close();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                openSegmentationPage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

    }
}