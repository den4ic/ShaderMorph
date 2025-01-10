#version 300 es
precision mediump float;

uniform vec2 iResolution;
uniform float morphFactor;
uniform int firstShapeIndex;
uniform int secondShapeIndex;
uniform bool isColorMode;

uniform vec3 internalColor;
uniform vec3 externalColor;

out vec4 fragColor;

// region SDF shapes
float sdCircle(in vec2 p, in float r) {
    return length(p) - r;
}

float sdSquare(in vec2 p, in float r) {
    p = abs(p);
    return max(p.x, p.y) - r;
}

float sdRectangle(in vec2 p, in float width, in float height) {
    p = abs(p);
    return max(p.x - width, p.y - height);
}

float sdOval(in vec2 p, in float rx, in float ry) {
    p = p / vec2(rx, ry);
    return length(p) - 1.0;
}

float sdDiamond(in vec2 p, in float r, in float aspect) {
    p = abs(p);
    p.x *= aspect;
    return (p.x + p.y - r) / aspect;
}

float sdParallelogram(in vec2 p, in float r) {
    vec2 a = vec2(0.0, 1.0);
    vec2 b = vec2(1.0, 0.5);
    vec2 q = vec2(dot(p, a), dot(p, b));
    return max(abs(q.x), abs(q.y)) - r;
}

float sdRegularPolygon(in vec2 p, in float r, in int n ) {
    float an = 3.141593/float(n);
    vec2 acs = vec2(cos(an),sin(an));

    float bn = mod(atan(p.x,p.y),2.0*an) - an;
    p = length(p)*vec2(cos(bn),abs(sin(bn)));

    p -= r*acs;
    p.y += clamp( -p.y, 0.0, r*acs.y);
    return length(p)*sign(p.x);
}

float sdTrapezoid(vec2 p, float halfWidth1, float halfWidth2, float halfHeight) {
    vec2 offset = vec2(0.5 * (halfWidth2 + halfWidth1), 0.0);
    p.x = abs(p.x) - offset.x;

    vec2 slope = vec2(0.5 * (halfWidth2 - halfWidth1), halfHeight);
    vec2 q = p - slope * clamp(dot(p, slope) / dot(slope, slope), -1.0, 1.0);

    float outsideDistance = length(q);
    float insideDistance = max(-outsideDistance, abs(p.y) - halfHeight);
    return mix(insideDistance, outsideDistance, step(0.0, q.x));
}

float cro(in vec2 a, in vec2 b) {
    return a.x * b.y - a.y * b.x;
}

float sdCapsule(in vec2 p, in float h) {
    float ra = 0.4;
    float rb = ra;

    p.x = abs(p.x);
    float b = (ra - rb) / h;
    vec2 c = vec2(sqrt(1.0 - b * b), b);
    float k = cro(c, p);
    float m = dot(c, p);
    float n = dot(p, p);

    if (k < 0.0) {
        return sqrt(n) - ra;
    } else if (k > c.x * h) {
        return sqrt(n + h * h - 2.0 * h * p.y) - rb;
    } else {
        return m - ra;
    }
}

float dot2(in vec2 v) { return dot(v, v); }

float sdHeart(in vec2 p, in float size, in float offsetY) {
    p.y += offsetY;
    p.x = abs(p.x);
    p *= size;

    if (p.y + p.x > 1.0)
        return sqrt(dot2(p - vec2(0.25, 0.75))) - sqrt(2.0) / 4.0;
    return sqrt(min(dot2(p - vec2(0.00, 1.00)),
                    dot2(p - 0.5 * max(p.x + p.y, 0.0)))) * sign(p.x - p.y);
}

float sdStar(in vec2 p, in float r, in float rf) {
    const vec2 k1 = vec2(0.809016994375, -0.587785252292);
    const vec2 k2 = vec2(-k1.x, k1.y);

    p.x = abs(p.x);
    p -= 2.0 * max(dot(k1, p), 0.0) * k1;
    p -= 2.0 * max(dot(k2, p), 0.0) * k2;
    p.x = abs(p.x);

    p.y -= r;
    vec2 ba = rf * vec2(-k1.y, k1.x) - vec2(0, 1);
    float h = clamp(dot(p, ba) / dot(ba, ba), 0.0, r);
    return length(p - ba * h) * sign(p.y * ba.x - p.x * ba.y);
}

float sdCrescent(vec2 p, float d, float ra, float rb) {
    p.y = abs(p.y);

    float a = (ra*ra - rb*rb + d*d)/(2.0*d);
    float b = sqrt(max(ra*ra-a*a,0.0));
    if(d*(p.x*b-p.y*a) > d*d*max(b-p.y,0.0)) {
        return length(p-vec2(a,b));
    }
    return max((length(p)-ra), -(length(p-vec2(d,0))-rb));
}

float getShapeDistance(int shapeIndex, vec2 p) {
    switch (shapeIndex) {
        case 0: return sdCircle(p, 0.7);
        case 1: return sdSquare(p, 0.7);
        case 2: return sdRectangle(p, 0.7, 0.5);
        case 3: return sdOval(p, 0.7, 0.5);
        case 4: return sdDiamond(p, 0.7, 1.5);
        case 5: return sdParallelogram(p, 0.5);
        case 6: return sdRegularPolygon(p, 0.7, 3);
        case 7: return sdRegularPolygon(p, 0.7, 5);
        case 8: return sdRegularPolygon(p, 0.7, 6);
        case 9: return sdRegularPolygon(p, 0.7, 7);
        case 10: return sdRegularPolygon(p, 0.7, 8);
        case 11: return sdRegularPolygon(p, 0.7, 9);
        case 12: return sdRegularPolygon(p, 0.7, 10);
        case 13: return sdTrapezoid(p, 0.6, 0.4, 0.6);
        case 14: return sdCapsule(p, 0.5);
        case 15: return sdHeart(p, 1.2, 0.5);
        case 16: return sdStar(p, 0.7, 0.5);
        case 17: return sdCrescent(p, 0.5, 0.8, 0.8);
        default: return sdCircle(p, 0.7);
    }
}
// endregion

void main() {
    vec2 p = (2.0 * gl_FragCoord.xy - iResolution.xy) / iResolution.y;

    float d1 = getShapeDistance(firstShapeIndex, p);
    float d2 = getShapeDistance(secondShapeIndex, p);

    float d = mix(d1, d2, morphFactor);

    vec3 col;
    if (isColorMode) {
        col = (d > 0.0) ? externalColor : internalColor;
        col *= 1.05 - exp(-6.0 * abs(d));
        col *= 0.8 + 0.2 * cos(110.0 * d);
        col = mix(col, vec3(1.0), 1.0 - smoothstep(0.0, 0.01, abs(d)));
    } else {
        col = (d > 0.0) ? vec3(1.0) : vec3(0.0);
    }

    fragColor = vec4(col, 1.0);
}