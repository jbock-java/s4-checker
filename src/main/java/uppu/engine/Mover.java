package uppu.engine;

import javafx.geometry.Point3D;
import uppu.model.Ball;
import uppu.model.Color;

public record Mover(
        Color color, // which ball to move
        Path path,
        Point3D detour) { // a detour will be taken if necessary, to avoid collision

    public Ball ball() {
        return color.sphere();
    }

    public Color source() {
        return path.source();
    }

    public Color destination() {
        return path.destination();
    }

    public Point3D detour2() {
        Point3D destination = destination().homePoint();
        Point3D source = source().homePoint();
        Point3D b = destination.subtract(source).multiply(0.5d);
        Point3D span = detour.subtract(source.add(destination).multiply(0.5d));
        return source.add(b).subtract(span);
    }
}
