package com.example.myapplication2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.vision.segmenter.ColoredLabel;
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter;
import org.tensorflow.lite.task.vision.segmenter.OutputType;
import org.tensorflow.lite.task.vision.segmenter.Segmentation;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class tfLiteActivity extends AppCompatActivity {

    private static final int ALPHA_VALUE = 128;
    String IMAGE_SEGMENTATION_MODEL = "deeplabv3_257_mv_gpu.tflite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tf_lite);

        // Initialization
        ImageSegmenter.ImageSegmenterOptions options = ImageSegmenter.ImageSegmenterOptions.builder().setOutputType(OutputType.CONFIDENCE_MASK).build();
        ImageSegmenter imageSegmenter = null;
        try {
            imageSegmenter = ImageSegmenter.createFromFile(this, IMAGE_SEGMENTATION_MODEL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap inputImage = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        TensorImage image = TensorImage.fromBitmap(inputImage);

        List<Segmentation> results = Objects.requireNonNull(imageSegmenter).segment(image);
        System.out.println(results);

        Pair<Bitmap, Map<String, Integer>> pair = createMaskBitmapAndLabels(
                results.get(0), inputImage.getWidth(),
                inputImage.getHeight()
        );

        Bitmap maskBitmap = pair.first;
        Map<String, Integer> itemsFound = pair.second;

        ImageView mImg;
        mImg = findViewById(R.id.tfLite);
        System.out.println(mImg);
        mImg.setImageBitmap(maskBitmap);

        System.out.println(maskBitmap);
        System.out.println(itemsFound);
    }

    private Pair<Bitmap, Map<String, Integer>> createMaskBitmapAndLabels(
            Segmentation result,
            int inputWidth,
            int inputHeight
    ) {
        // For the sake of this demo, change the alpha channel from 255 (completely opaque) to 128
        // (semi-transparent), because the maskBitmap will be stacked over the original image later.
        List<ColoredLabel> coloredLabels = result.getColoredLabels();
        int[] colors = new int[coloredLabels.size()];
        int cnt = 0;
        for (ColoredLabel coloredLabel : coloredLabels) {
            int rgb = coloredLabel.getArgb();
            System.out.println(Color.red(rgb));
            colors[cnt++] = Color.argb(ALPHA_VALUE, Color.red(rgb), Color.green(rgb), Color.blue(rgb));
        }
        // Use completely transparent for the background color.
        colors[0] = Color.TRANSPARENT;
        System.out.println(Arrays.toString(colors));
        // Create the mask bitmap with colors and the set of detected labels.
        TensorImage maskTensor = result.getMasks().get(0);
        byte[] maskArray = maskTensor.getBuffer().array();
        int[] pixels = new int[maskArray.length];
        HashMap<String, Integer> itemsFound = new HashMap<String, Integer>();
        for (int i = 0; i < maskArray.length; i++) {
            int colorIndex = maskArray[i];
            int color = colors[colorIndex];
            pixels[i] = color;
            itemsFound.put(coloredLabels.get(maskArray[i]).getlabel(), color);
        }
        Bitmap maskBitmap = Bitmap.createBitmap(
                pixels, maskTensor.getWidth(), maskTensor.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        // Scale the maskBitmap to the same size as the input image.
        System.out.println(itemsFound);
        return new Pair<Bitmap, Map<String, Integer>>(Bitmap.createScaledBitmap(maskBitmap, inputWidth, inputHeight, true), itemsFound);
    }

}