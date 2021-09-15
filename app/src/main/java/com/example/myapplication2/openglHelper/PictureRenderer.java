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

import com.example.myapplication2.EditImageActivity;
import com.example.myapplication2.R;
import com.example.myapplication2.utils.MatrixHelper;
import com.example.myapplication2.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;


public class PictureRenderer implements Renderer {
    private final Context context;


    private final Matrix4f matrix = new Matrix4f();
    private final Matrix4f matrix2 = new Matrix4f();

    private Picture picture;
    private Picture picture2;
    public Bitmap pic;
    public Bitmap savedPicture;





    private TextureShaderProgram textureProgram;
    private TextureShaderProgram textureProgram2;

    private int checkWidth;
    private int checkHeight;

    private int texture;
    private int texture2;


    public PictureRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        Bitmap mock =  BitmapFactory.decodeResource(context.getResources(),
                R.drawable.mock_ido);

        picture = new Picture();
        picture2 = new Picture();

        TextureHelper.setTextureHandle(2);

        textureProgram = new TextureShaderProgram(context);
        textureProgram2 = new TextureShaderProgram(context);


        texture = TextureHelper.loadTexture(pic,0);
        texture2 = TextureHelper.loadTexture(pic,1);

    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
        setPictureScale(width, height);
        movePicture(0.5f,0);

        checkWidth = width;
        checkHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);


        textureProgram2.useProgram();
        textureProgram2.setUniforms(matrix2.getArray(), texture2, 1f);
        picture2.bindData(textureProgram2);
        picture2.draw();

        glEnable( GL_BLEND );
        glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );

        // Draw the table.
        textureProgram.useProgram();
        textureProgram.setUniforms(matrix.getArray(), texture, 0.5f);
        picture.bindData(textureProgram);
        picture.draw();

        saveChanges(checkWidth, checkHeight);





    }

    private void setPictureScale(int viewWidth, int viewHeight){

        final float aspectRatio = viewWidth > viewHeight ?
                (float) viewWidth / (float) viewHeight :
                (float) viewHeight / (float) viewWidth;
        if (viewWidth > viewHeight) {
            matrix.loadOrtho(-aspectRatio, aspectRatio,-1f,1f,-1f,1f);
        } else {
            matrix.loadOrtho(-1f,1f,-aspectRatio, aspectRatio,-1f,1f);
        }

    }

    private void movePicture(float x, float y){
        matrix.loadTranslate(x,y,0);
    }

    public void saveChanges(int width, int height){
        int screenshotSize = width * height;
        ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
        bb.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bb);
        int pixelsBuffer[] = new int[screenshotSize];
        bb.asIntBuffer().get(pixelsBuffer);
        bb = null;

        for (int i = 0; i < screenshotSize; ++i) {
            pixelsBuffer[i] = ((pixelsBuffer[i] & 0xff00ff00)) |    ((pixelsBuffer[i] & 0x000000ff) << 16) | ((pixelsBuffer[i] & 0x00ff0000) >> 16);
        }

        savedPicture = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        savedPicture.setPixels(pixelsBuffer, screenshotSize-width, -width, 0, 0, width, height);

    }





}