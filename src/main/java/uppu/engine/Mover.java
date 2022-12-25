package uppu.engine;

import javafx.geometry.Point3D;
import uppu.model.Ball;
import uppu.model.Color;

public record Mover(
        Color color, // which ball to move
        Path path,
        Point3D detour1, // a detour will be taken if necessary, to avoid collision
        Point3D detour2) { // a detour will be taken if necessary, to avoid collision

    public Ball ball() {
        return color.sphere();
    }

    public Path normalize() {
        return path.normalize();
    }

    public Color source() {
        return path.source();
    }

    public Color destination() {
        return path.destination();
    }

    public Point3D noDetour() {
        return source().homePoint().add(destination().homePoint()).multiply(0.5d);
    }
}
