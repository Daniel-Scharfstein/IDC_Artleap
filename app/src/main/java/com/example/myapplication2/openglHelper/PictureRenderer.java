package com.example.myapplication2.openglHelper;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.renderscript.Matrix4f;

import com.example.myapplication2.utils.EditParametersSplitColors;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class PictureRenderer implements Renderer {
    private final Context context;

    private float[] opacity = {(float) 0.5, (float) 0.7, (float) 0.6};
    private Matrix4f[] matrix = new Matrix4f[3];
    private Picture[] pictures = new Picture[3];

    public Bitmap pic;
    public Bitmap savedPicture;

    private TextureShaderProgram[] textureShaderPrograms = new TextureShaderProgram[3];

    private int checkWidth;
    private int checkHeight;

    private int[] texture = new int[3];

    public EditParametersSplitColors currentParameters = new EditParametersSplitColors(0.0, 0.0, 0.0);


    public PictureRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        TextureHelper.setTextureHandle(4);

        for (int i = 0; i < 3; i++) {
            pictures[i] = new Picture();
            textureShaderPrograms[i] = new TextureShaderProgram(context);
            texture[i] = TextureHelper.loadTexture(pic, i);
            matrix[i] = new Matrix4f();
        }



    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);


        Matrix4f[] moveMatrix = new Matrix4f[3];
        Matrix4f[] scaleMatrix = new Matrix4f[3];
        moveMatrix[0] = new Matrix4f();
        moveMatrix[1] = movePicture((float) currentParameters.getSpreadX(), (float) currentParameters.getSpreadY(),1);
        moveMatrix[2] = movePicture(-(float) currentParameters.getSpreadX(), -(float) currentParameters.getSpreadY(), 2);

        for (int i = 0; i < 3; i++) {
//            scaleMatrix[i] = setPictureScale(width, height, i);
            scaleMatrix[i] = new Matrix4f();
            matrix[i].loadMultiply(moveMatrix[i],scaleMatrix[i]);
        }





        checkWidth = width;
        checkHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (currentParameters.getSpreadX() == 0 && currentParameters.getSpreadY() == 0){
            opacity[0] = 1;
        }
        else
        {
            opacity[0] = 0.5f;
        }


        for (int i = 0; i < 3; i++) {
            textureShaderPrograms[2-i].useProgram();
            textureShaderPrograms[2-i].setUniforms(matrix[2-i].getArray(), texture[2-i], opacity[2-i], (float) currentParameters.getColor());
            pictures[2-i].bindData(textureShaderPrograms[2-i]);
            pictures[2-i].draw();
        }

        saveChanges(checkWidth, checkHeight);

    }

    private Matrix4f setPictureScale(int viewWidth, int viewHeight, int id) {
        Matrix4f ortho = new Matrix4f();

        final float aspectRatio = viewWidth > viewHeight ?
                (float) viewWidth / (float) viewHeight :
                (float) viewHeight / (float) viewWidth;
        if (viewWidth > viewHeight) {
            ortho.loadOrtho(-aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            ortho.loadOrtho(-1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        return ortho;
//        matrix[id].load(mat);
    }

    private Matrix4f movePicture(float x, float y, int id) {
        Matrix4f move = new Matrix4f();
        move.loadTranslate(x, y, 0);
        return move;
    }

    public void saveChanges(int width, int height) {
        int screenshotSize = width * height;
        ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
        bb.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bb);
        int pixelsBuffer[] = new int[screenshotSize];
        bb.asIntBuffer().get(pixelsBuffer);
        bb = null;

        for (int i = 0; i < screenshotSize; ++i) {
            pixelsBuffer[i] = ((pixelsBuffer[i] & 0xff00ff00)) | ((pixelsBuffer[i] & 0x000000ff) << 16) | ((pixelsBuffer[i] & 0x00ff0000) >> 16);
        }

        savedPicture = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        savedPicture.setPixels(pixelsBuffer, screenshotSize - width, -width, 0, 0, width, height);

    }

}