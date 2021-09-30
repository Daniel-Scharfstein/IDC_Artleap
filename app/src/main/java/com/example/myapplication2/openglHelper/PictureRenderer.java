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

    private float[] opacity = {(float) 1, (float) 0.5, (float) 0.5};
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
            opacity[2] = 0;
            opacity[1] = 0;
        }
        else
        {
            opacity[2] = 0.5f;
            opacity[1] = 0.5f;
        }

        textureShaderPrograms[2].useProgram();
        textureShaderPrograms[2].setUniforms(matrix[2].getArray(), texture[2], opacity[2], (float) currentParameters.getColor());
        pictures[2].bindData(textureShaderPrograms[2]);
        pictures[2].draw();

        textureShaderPrograms[0].useProgram();
        textureShaderPrograms[0].setUniforms(matrix[0].getArray(), texture[0], opacity[0], (float) currentParameters.getColor());
        pictures[0].bindData(textureShaderPrograms[0]);
        pictures[0].draw();

        textureShaderPrograms[1].useProgram();
        textureShaderPrograms[1].setUniforms(matrix[1].getArray(), texture[1], opacity[1], (float) currentParameters.getColor());
        pictures[1].bindData(textureShaderPrograms[1]);
        pictures[1].draw();



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