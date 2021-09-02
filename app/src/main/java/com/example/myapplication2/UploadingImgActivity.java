package com.example.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;


public class UploadingImgActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    public static final String EXTRA_MESSAGE = "com.example.myapplication2.MESSAGE";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo);
        Button buttonLoadImage = findViewById(R.id.buttonLoadPicture);
//        buttonLoadImage.setOnClickListener(arg0 -> {
//            Intent i = new Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(i, RESULT_LOAD_IMAGE);
//        });
        buttonLoadImage.setOnClickListener(view -> drawOpenGL());
        Button buttonUploadImage = findViewById(R.id.buttonUploadPhoto);
        buttonUploadImage.setOnClickListener(arg0 -> openLoadingPage());

    }

    public void drawOpenGL(){
        Intent intent = new Intent(this, Run.class);
        startActivity(intent);
    }

//    public void drawOpenGL(Bitmap pic){
//        Intent intent = new Intent(this, OpenGLES20Activity.class);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        intent.putExtra("picture", byteArray);
//        startActivity(intent);
//    }

    public void openLoadingPage() {
        Intent intent = new Intent(this, LoadingScreenActivity.class);
        startActivity(intent);
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
            ImageView imageView = findViewById(R.id.imgView);
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

