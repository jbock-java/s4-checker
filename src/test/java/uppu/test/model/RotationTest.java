package uppu.test.model;

import javafx.geometry.Point3D;
import org.junit.jupiter.api.Test;
import uppu.model.HomePoints;
import uppu.model.Rotation;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RotationTest {

    @Test
    void testZero() {
        Point3D p = new Point3D(1, 2, 3);
        Rotation rotation = Rotation.fromAxis(new Point3D(4, 2, 5));
        Point3D result = rotation.apply(p, 0);
        assertTrue(p.distance(result) < 0.000001);
    }

    @Test
    void testFromAxis() {
        Point3D p0 = HomePoints.homePoints().get(0);
        Point3D p1 = HomePoints.homePoints().get(1);
        Point3D p2 = HomePoints.homePoints().get(2);
        Point3D center = p0.add(p1).add(p2).multiply(1f / 3f);
        Point3D v = p0.subtract(center);
        Rotation rotation = Rotation.fromAxis(center);
        Point3D result = rotation.apply(v, -Math.PI * 2f / 3f);
        assertTrue(p1.distance(center.add(result)) < 0.000001);
    }
}