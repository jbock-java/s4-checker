package uppu.model;

public final class Point {

    private static final float HOME_RADIUS_100 = 12;
    private final float x;
    private final float y;
    private final float z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Point scale(float scale) {
        return new Point(x * scale, y * scale, z);
    }
    
    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }
}
