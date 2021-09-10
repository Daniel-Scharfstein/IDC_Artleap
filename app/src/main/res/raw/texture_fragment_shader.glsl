precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
vec4 red = vec4(1.0,0.0,0.0,0.0);
vec4 blue = vec4(0.2,0.96,1.000,1.000);
void main()
{

    gl_FragColor =  texture2D(u_TextureUnit, v_TextureCoordinates);
}