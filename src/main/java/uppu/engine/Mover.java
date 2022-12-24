package uppu.engine;

import javafx.geometry.Point3D;
import uppu.model.Ball;
import uppu.model.Color;

public record Mover(Color color, Point3D source, Point3D target) {

    public Ball ball() {
        return color.ball();
    }
}
