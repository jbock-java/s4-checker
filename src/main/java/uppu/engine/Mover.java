package uppu.engine;

import javafx.geometry.Point3D;
import uppu.model.Ball;
import uppu.model.Color;

public record Mover(
        Color color, // which ball to move
        Path path,
        Point3D span) { // a detour will be taken if necessary, to avoid collision

    public Ball ball() {
        return color.sphere();
    }

    public Color source() {
        return path.source();
    }

    public Color destination() {
        return path.destination();
    }

    public Point3D span2() {
        return span.multiply(-1);
    }
}
