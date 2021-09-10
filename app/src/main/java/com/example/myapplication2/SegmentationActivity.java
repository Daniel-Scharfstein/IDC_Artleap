package com.example.myapplication2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.segmentation.Segmentation;
import com.google.mlkit.vision.segmentation.SegmentationMask;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions;

import java.nio.ByteBuffer;

public class SegmentationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tf_lite);

        SelfieSegmenterOptions options =
                new SelfieSegmenterOptions.Builder()
                        .setDetectorMode(SelfieSegmenterOptions.STREAM_MODE)
                        .enableRawSizeMask()
                        .build();

        Segmenter segmenter = Segmentation.getClient(options);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);

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

                                        Bitmap proccessBitMap =
                                                Bitmap.createBitmap(
                                                        maskColorsFromByteBuffer(maskBuffer, maskWidth, maskHeight, bitmap), maskWidth, maskHeight, Bitmap.Config.ARGB_8888);
                                        System.out.println("daniel");
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
    }

    /**
     * Converts byteBuffer floats to ColorInt array that can be used as a mask.
     */
    @ColorInt
    private int[] maskColorsFromByteBuffer(ByteBuffer byteBuffer, int maskWidth, int maskHeight, Bitmap image) {
        @ColorInt int[] colors = new int[maskWidth * maskHeight];

        for (int i = 0; i < maskWidth * maskHeight; i++) {
            System.out.println(i);
            float backgroundLikelihood = 1 - byteBuffer.getFloat();
            if (backgroundLikelihood > 0.9) {
                colors[i] = Color.argb(128, 255, 0, 255);
            } else if (backgroundLikelihood > 0.2) {
                // Linear interpolation to make sure when backgroundLikelihood is 0.2, the alpha is 0 and
                // when backgroundLikelihood is 0.9, the alpha is 128.
                // +0.5 to round the float value to the nearest int.
                int alpha = (int) (182.9 * backgroundLikelihood - 36.6 + 0.5);
                colors[i] = Color.argb(alpha, 255, 0, 255);
            } else {
                colors[i] = image.getPixel(125, 125);
            }
        }
        System.out.println(colors);
        return colors;
    }

}