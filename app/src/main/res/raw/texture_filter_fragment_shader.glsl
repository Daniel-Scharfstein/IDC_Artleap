precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying float v_Percentage;
varying float v_Color;
vec4 texture;
vec4 temp;
vec4 blue = vec4(0,0,0.9,1);
vec4 green = vec4(0,0.39,0,1);
vec4 yellow = vec4(1,1,0,1);
vec4 red = vec4(1,0,0,1);


void main()
{
    if(v_Color == 0.0){
        texture = texture2D(u_TextureUnit, v_TextureCoordinates);
    }
    else if(v_Color == 1.0){
        temp = blue;
        blue.r = temp.r + 0.4*v_Percentage;
        blue.g = temp.g + 0.4*v_Percentage;
        blue.b = temp.b - 0.4*v_Percentage;
        texture = blue * texture2D(u_TextureUnit, v_TextureCoordinates);
    }
    else if(v_Color == 2.0){
        temp = green;
        green.r = temp.r + 0.15*v_Percentage;
        green.g = temp.g - 0.15*v_Percentage;
        green.b = temp.b + 0.15*v_Percentage;

        texture = green * texture2D(u_TextureUnit, v_TextureCoordinates);

    }
    else if(v_Color == 3.0){
        temp = yellow;
        yellow.r = temp.r - 0.3*v_Percentage;
        yellow.g = temp.g - 0.3*v_Percentage;
        yellow.b = temp.b + 0.3*v_Percentage;
        texture = yellow * texture2D(u_TextureUnit, v_TextureCoordinates);
    }
    else if(v_Color == 4.0){
        temp = red;
        red.r = temp.r - 0.3*v_Percentage;
        red.g = temp.g + 0.3*v_Percentage;
        red.b = temp.b + 0.3*v_Percentage;
        texture = red * texture2D(u_TextureUnit, v_TextureCoordinates);
    }


    gl_FragColor = texture;
}