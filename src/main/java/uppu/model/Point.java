package uppu.model;

import javafx.scene.canvas.GraphicsContext;

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
    }

    Point scale(float scale) {
        return new Point(x * scale, y * scale, z);
    }

    public void paintHome(GraphicsContext g, Quadruple quadruple) {
        float centerX = x() + quadruple.getOffsetX() + ((Action.BALL_SIZE - homeRadius) / 2f);
        float centerY = y() + quadruple.getOffsetY() + ((Action.BALL_SIZE - homeRadius) / 2f);
        g.fillOval(centerX - homeRadius, centerY - homeRadius, homeRadius, homeRadius);
        g.fillOval(centerX - homeRadius, centerY + homeRadius, homeRadius, homeRadius);
        g.fillOval(centerX + homeRadius, centerY - homeRadius, homeRadius, homeRadius);
        g.fillOval(centerX + homeRadius, centerY + homeRadius, homeRadius, homeRadius);
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
