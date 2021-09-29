uniform mat4 u_Matrix;
uniform float u_Percentage;
uniform float u_Color;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;
varying float v_Percentage;
varying float v_Color;



void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    v_Percentage = u_Percentage;
    v_Color = u_Color;
    gl_Position =  u_Matrix * a_Position;
}