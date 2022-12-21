package uppu.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Ellipse;

import java.util.Objects;

public final class Point {

    private static final float HOME_RADIUS_100 = 12;
    private final float x;
    private final float y;
    private final float z;
    private final float homeRadius;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.homeRadius = HOME_RADIUS_100 / z;
        this.home = new Ellipse(0, 0, homeRadius, homeRadius);
    }

    private final Ellipse home;

    Point scale(float scale) {
        return new Point(x * scale, y * scale, z);
    }

    public void paintHome(GraphicsContext g) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Point) obj;
        return Float.floatToIntBits(this.x) == Float.floatToIntBits(that.x) &&
                Float.floatToIntBits(this.y) == Float.floatToIntBits(that.y) &&
                Float.floatToIntBits(this.z) == Float.floatToIntBits(that.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Point[" +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "z=" + z + ']';
    }
}
