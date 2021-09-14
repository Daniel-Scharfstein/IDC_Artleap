uniform mat4 u_Matrix;
uniform float u_Opacity;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;
varying float v_Opacity;

void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    v_Opacity = u_Opacity;
    gl_Position =  u_Matrix * a_Position;
}