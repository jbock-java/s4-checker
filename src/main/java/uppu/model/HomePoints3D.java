package uppu.model;

import javafx.geometry.Point3D;

import java.util.List;
import java.util.function.Supplier;

import static uppu.util.Suppliers.memoize;

public final class HomePoints3D {

    private static final Supplier<List<Point3D>> HOME_POINTS = memoize(() ->
            getHomePoints().stream().map(p -> p.multiply(10)).toList());

    public static List<Point3D> homePoints() {
        return HOME_POINTS.get();
    }

    private static List<Point3D> getHomePoints() {
        float r0 = (float) (Math.sqrt(2) / 3f);
        float r1 = (float) (Math.sqrt(2) / Math.sqrt(3));
        return List.of(
                new Point3D(2 * r0, 0, -1f / 3f),
                new Point3D(-r0, r1, -1f / 3f),
                new Point3D(-r0, -r1, -1f / 3f),
                new Point3D(0, 0, 1));
    }

    private HomePoints3D() {
    }
}
