package uppu.model;

import javafx.scene.shape.DrawMode;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static uppu.util.Suppliers.memoize;

public final class Spheres {

    private final Ball redSphere = Ball.create(Color.RED, 1.2f, DrawMode.FILL).build();
    private final Ball greenSphere = Ball.create(Color.GREEN, 1.2f, DrawMode.FILL).build();
    private final Ball blueSphere = Ball.create(Color.BLUE, 1.2f, DrawMode.FILL).build();
    private final Ball silverSphere = Ball.create(Color.SILVER, 1.2f, DrawMode.FILL).build();
    private final Ball redHome = Ball.create(Color.RED, 2f, DrawMode.LINE).build(32);
    private final Ball greenHome = Ball.create(Color.GREEN, 2f, DrawMode.LINE).build(32);
    private final Ball blueHome = Ball.create(Color.BLUE, 2f, DrawMode.LINE).build(32);
    private final Ball silverHome = Ball.create(Color.SILVER, 2f, DrawMode.LINE).build(32);

    private static final Supplier<Spheres> INSTANCE = memoize(Spheres::new);

    public static Spheres spheres() {
        return INSTANCE.get();
    }

    private final Supplier<Map<Color, Ball>> byColor = memoize(() -> {
        EnumMap<Color, Ball> m = new EnumMap<>(Color.class);
        m.put(Color.GREEN, greenSphere);
        m.put(Color.RED, redSphere);
        m.put(Color.BLUE, blueSphere);
        m.put(Color.SILVER, silverSphere);
        return m;
    });

    private final Supplier<Map<Color, Ball>> homeByColor = memoize(() -> {
        EnumMap<Color, Ball> m = new EnumMap<>(Color.class);
        m.put(Color.GREEN, greenHome);
        m.put(Color.RED, redHome);
        m.put(Color.BLUE, blueHome);
        m.put(Color.SILVER, silverHome);
        return m;
    });

    public Ball get(Color color) {
        return byColor.get().get(color);
    }

    public Ball getHome(Color color) {
        return homeByColor.get().get(color);
    }
}
