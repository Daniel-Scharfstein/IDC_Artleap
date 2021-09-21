package com.example.myapplication2;

import static com.example.myapplication2.HomeActivity.tempFileImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.segmentation.Segmentation;
import com.google.mlkit.vision.segmentation.SegmentationMask;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Objects;

public class SegmentationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.segmentation_processing);
        Objects.requireNonNull(getSupportActionBar()).hide();

        SelfieSegmenterOptions options =
                new SelfieSegmenterOptions.Builder()
                        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
//                        .enableRawSizeMask()
                        .build();

        Segmenter segmenter = Segmentation.getClient(options);

        String filePath = getIntent().getStringExtra("path");
        File file = new File(filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<SegmentationMask> result =
                segmenter.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<SegmentationMask>() {
                                    @Override
                                    public void onSuccess(SegmentationMask mask) {
                                        ByteBuffer maskBuffer = mask.getBuffer();
                                        int maskWidth = mask.getWidth();
                                        int maskHeight = mask.getHeight();

                                        Bitmap processBitMap =
                                                Bitmap.createBitmap(
                                                        maskColorsFromByteBuffer(maskBuffer, maskWidth, maskHeight), maskWidth, maskHeight, Bitmap.Config.ARGB_8888);
                                        Bitmap testBitmap = colorBitmap(processBitMap, bitmap);
                                        openEditPage(testBitmap);
                                    }
                                });
    }

    public void openEditPage(Bitmap pic) {
        Intent intent = new Intent(this, EditImageActivity.class);
        String filePath = tempFileImage(this, pic, "tempImage");
        intent.putExtra("path", filePath);
        startActivity(intent);
    }

    /**
     * Converts byteBuffer floats to ColorInt array that can be used as a mask.
     */
    @ColorInt
    private int[] maskColorsFromByteBuffer(ByteBuffer byteBuffer, int maskWidth, int maskHeight) {
        @ColorInt int[] colors = new int[maskWidth * maskHeight];
        for (int i = 0; i < maskWidth * maskHeight; i++) {
            float backgroundLikelihood = 1 - byteBuffer.getFloat();
            if (backgroundLikelihood > 0.9) {
                colors[i] = Color.TRANSPARENT;
            } else if (backgroundLikelihood > 0.2) {
                // Linear interpolation to make sure when backgroundLikelihood is 0.2, the alpha is 0 and
                // when backgroundLikelihood is 0.9, the alpha is 128.
                // +0.5 to round the float value to the nearest int.
                colors[i] = Color.argb(128,0,0,0);
            } else {
                colors[i] = Color.WHITE;
            }
        }
        return colors;
    }

    private Bitmap colorBitmap(Bitmap bitmap, Bitmap image) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                if (bitmap.getPixel(x, y) == Color.WHITE) {
                    bitmap.setPixel(x, y, image.getPixel(x, y));
                }
            }
        }
        return bitmap;
    }
}