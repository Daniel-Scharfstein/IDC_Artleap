precision mediump float;
uniform sampler2D u_TextureUnit;
varying float v_Opacity;
varying vec2 v_TextureCoordinates;
varying float v_Color;
varying float v_Id;



vec4 red = vec4(1,0.0,0.0,1);

vec4 blue = vec4(0.0,0.0,1, 1);
void main()
{
    vec4 texture = texture2D(u_TextureUnit, v_TextureCoordinates);

    if(texture.r == 0.0 && texture.g == 0.0 && texture.b == 0.0){
        gl_FragColor = vec4(0.0);
        return;
    }

    texture.a = v_Opacity;

    if(v_Id == 2.0){
        blue.r = v_Color/100;
        gl_FragColor =  blue * texture;
        return;
    }
    else if(v_Id == 3.0){
        red.g = v_Color/100;
        gl_FragColor = red * texture;
        return;
    }

    gl_FragColor = texture;
}