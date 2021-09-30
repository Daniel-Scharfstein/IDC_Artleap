package com.example.artleap_android.openglHelper;

import android.content.Context;
import android.opengl.GLES20;

import com.example.artleap_android.R;

public class TextureFilterShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrix;
    private final int uTextureUnitLocation;
    private final int uColor;
    private final int uPercentage;





    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureFilterShaderProgram(Context context) {
        super(context, R.raw.texture_filter_vertex_shader,
                R.raw.texture_filter_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrix = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);
        uPercentage = GLES20.glGetUniformLocation(program, U_PERCENTAGE);
        uColor = GLES20.glGetUniformLocation(program, U_COLOR);


        // Retrieve attribute locations for the shader program.
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation =
                GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);


    }



    public void setUniforms(float[] matrix, int textureId, float color, float percentage) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix, 0);

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        GLES20.glUniform1i(uTextureUnitLocation, 0);
        GLES20.glUniform1f(uPercentage, percentage);
        GLES20.glUniform1f(uColor, color);


    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}