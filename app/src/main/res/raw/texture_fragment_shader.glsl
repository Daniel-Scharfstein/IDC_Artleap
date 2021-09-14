precision mediump float;
uniform sampler2D u_TextureUnit;
//uniform sampler2D u_TextureUnit2;
varying float v_Opacity;
varying vec2 v_TextureCoordinates;
vec4 red = vec4(1.0,0.0,0.0,0.0);
vec4 blue = vec4(0.2,0.96,1.000,1.000);
void main()
{
    vec4 texture = texture2D(u_TextureUnit, v_TextureCoordinates);
//    vec4 texture2 = texture2D(u_TextureUnit2, v_TextureCoordinates);
//
//    if(texture2.r == 0.0){
//        gl_FragColor = vec4(0.0);
//        return;
//    }

    texture.a = v_Opacity;
    gl_FragColor =  texture;
}