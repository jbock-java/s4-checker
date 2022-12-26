package uppu.model;

import javafx.geometry.Point3D;

public record Rotation(Point3D row0, Point3D row1, Point3D row2) {

    public static Rotation fromAxis(Point3D u, double angle) {
        double ux = u.getX();
        double uy = u.getY();
        double uz = u.getZ();
        return null;
    }
}
