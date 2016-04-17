#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_scale;

varying vec4 v_color;
varying vec2 v_texCoord;


void main() {
    float smoothing = .25 / (4. * u_scale);
    float distance = texture2D(u_texture, v_texCoord).a;
    float alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance) * v_color.a;
    vec4 color = vec4(v_color.rgb, alpha);
    if ( distance < 0.5 + smoothing ) color = vec4(v_color.r * alpha,v_color.g *alpha, v_color.b *alpha, distance*2. * v_color.a);
    gl_FragColor = color;
}