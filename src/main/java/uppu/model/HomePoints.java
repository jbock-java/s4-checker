package uppu.model;

import java.util.List;

public final class HomePoints {

    public static final float SCALE = 2.0f;
    public static final int MAX = (int) (200 * SCALE);

    static List<Point> homePoints(int n) {
        return getHomePoints(n).stream().map(p -> p.scale(SCALE)).toList();
    }

    private static List<Point> getHomePoints(int n) {
        switch (n) {
            case 4:
                return List.of(
                        new Point(90.06664f, 0.0f, 1.25f),
                        new Point(180.13327f, 156.0f, 1.0f),
                        new Point(0f, 156.0f, 1.0f),
                        new Point(90.06664f, 104.0f, 1.5f));
            case 5:
                return List.of(
                        new Point(100.0f, 0.0f, 0.0f),
                        new Point(195.10565162951536f, 69.09830056250524f, 0.0f),
                        new Point(158.77852522924732f, 180.90169943749473f, 0.0f),
                        new Point(41.2214747707527f, 180.90169943749476f, 0.0f),
                        new Point(4.894348370484636f, 69.09830056250527f, 0.0f));
        }
        throw new IllegalArgumentException("" + n);
    }

    private HomePoints() {
    }
}
