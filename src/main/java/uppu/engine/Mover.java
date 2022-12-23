package uppu.engine;

import javafx.geometry.Point3D;
import uppu.model.Color;
import uppu.model.Point;
import uppu.model.Quadruple;

public record Mover(Color color, Point3D source, Point3D target) {
}
