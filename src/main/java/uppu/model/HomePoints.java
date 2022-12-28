package uppu.model;

import io.parmigiano.Permutation;
import javafx.geometry.Point3D;

import java.util.List;
import java.util.function.Supplier;

import static uppu.util.Suppliers.memoize;

public final class HomePoints {

    public static final double EDGE_LENGTH = Math.sqrt(8d / 3d);
    public static final double FACE_RADIUS = EDGE_LENGTH * (2d / 3d) * (Math.sqrt(3) / 2);

    private static final Supplier<List<Point3D>> HOME_POINTS = memoize(() ->
            getHomePoints().stream()
                    .map(p -> permute(p, Permutation.cycle(0, 1).compose(0, 2)))
                    .map(p -> new Point3D(p.getX(), -p.getY(), p.getZ()))
                    .toList());

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

    private static Point3D permute(Point3D point, Permutation p) {
        List<Double> doubles = List.of(point.getX(), point.getY(), point.getZ());
        List<Double> result = p.apply(doubles);
        return new Point3D(result.get(0), result.get(1), result.get(2));
    }

    static Point3D homePoint(Colour color) {
        return switch (color) {
            case RED -> homePoints().get(0);
            case GREEN -> homePoints().get(1);
            case BLUE -> homePoints().get(2);
            case SILVER -> homePoints().get(3);
        };
    }

    private HomePoints() {
    }
}
