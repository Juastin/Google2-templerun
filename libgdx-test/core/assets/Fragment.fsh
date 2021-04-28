out vec4 gl_FragColor;
varying vec2 v_texCoords;
uniform float cutoff = 0.5f;
uniform sampler2D u_texture;

void main() {
    vec2 uv = v_texCoords;
    float color_pixel = texture2D(u_texture, uv).r;
    if (color_pixel < cutoff) {
        gl_FragColor = vec4(gl_FragColor.rgb, 0.00);
    } else {
        gl_FragColor = vec4(gl_FragColor.rgb, 1.00);
    }
}
//out vec4 gl_FragColor;
//in vec2 v_vTexcoord;
//uniform sampler2D u_texture;
//
//uniform float time;
//
//void main(){
//    gl_FragColor = texture2D( u_texture, i.texcoord.xy).r;
//
//    if (gl_FragColor.r > time) gl_FragColor.a = 0.0;
//
//    gl_FragColor.rgb = vec3(0.0);
//}