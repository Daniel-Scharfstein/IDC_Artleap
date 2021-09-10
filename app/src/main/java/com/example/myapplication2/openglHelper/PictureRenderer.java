package com.example.myapplication2.openglHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.myapplication2.R;
import com.example.myapplication2.utils.MatrixHelper;
import com.example.myapplication2.utils.TextResourceReader;


public class PictureRenderer implements Renderer {
    private final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final Matrix4f matrix = new Matrix4f();

    private Picture picture;
    public Bitmap pic;

    private TextureShaderProgram textureProgram;

    private int texture;

    public PictureRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


//        Bitmap mock =  BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.mock_ido);

        picture = new Picture();
        TextureHelper.setTextureHandle(2);

        textureProgram = new TextureShaderProgram(context);

        texture = TextureHelper.loadTexture(pic,0);

    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
        setPictureScale(height, width);

//        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
//                / (float) height, 1f, 10f);

//        setIdentityM(modelMatrix, 0);
//        translateM(modelMatrix, 0, 0f, 0f, -2.5f);

//        final float[] temp = new float[16];
//        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        // Draw the table.
        textureProgram.useProgram();
        textureProgram.setUniforms(matrix.getArray(), texture);

        picture.bindData(textureProgram);
        picture.draw();

    }

    private void setPictureScale(int viewHeight, int viewWidth){

        final float aspectRatio = viewWidth > viewHeight ?
                (float) viewWidth / (float) viewHeight :
                (float) viewHeight / (float) viewWidth;
        if (viewWidth > viewHeight) {
            matrix.loadOrtho(-aspectRatio, aspectRatio,-1f,1f,-1f,1f);
        } else {
            matrix.loadOrtho(-1f,1f,-aspectRatio, aspectRatio,-1f,1f);
        }
    }
}