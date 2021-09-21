uniform mat4 u_Matrix;
uniform float u_Opacity;
uniform float u_Color;
uniform float u_Id;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;
varying float v_Opacity;
varying float v_Color;
varying float v_Id;


void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    v_Opacity = u_Opacity;
    v_Color = u_Color;
    v_Id = u_Id;
    gl_Position =  u_Matrix * a_Position;
}