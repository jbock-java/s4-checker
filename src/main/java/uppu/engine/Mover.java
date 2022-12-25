package uppu.engine;

import javafx.geometry.Point3D;
import uppu.model.Ball;
import uppu.model.Color;

public record Mover(
        Color color, // which ball to move
        Path path) {

    public Ball ball() {
        return color.sphere();
    }

    public Color source() {
        return path.source();
    }

    public Color destination() {
        return path.destination();
    }

    public Point3D midPoint() {
        return path.midPoint();
    }
}
