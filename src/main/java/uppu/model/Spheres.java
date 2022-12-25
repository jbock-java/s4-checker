package uppu.model;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static uppu.util.Suppliers.memoize;

final class Spheres {

    private final Ball redSphere = Ball.create(Color.RED, 0.24f, DrawMode.FILL).build();
    private final Ball greenSphere = Ball.create(Color.GREEN, 0.24f, DrawMode.FILL).build();
    private final Ball blueSphere = Ball.create(Color.BLUE, 0.24f, DrawMode.FILL).build();
    private final Ball silverSphere = Ball.create(Color.SILVER, 0.24f, DrawMode.FILL).build();
    private final Ball redHome = Ball.create(Color.RED, 0.4f, DrawMode.LINE).build(32);
    private final Ball greenHome = Ball.create(Color.GREEN, 0.4f, DrawMode.LINE).build(32);
    private final Ball blueHome = Ball.create(Color.BLUE, 0.4f, DrawMode.LINE).build(32);
    private final Ball silverHome = Ball.create(Color.SILVER, 0.4f, DrawMode.LINE).build(32);

    private static final Supplier<Spheres> INSTANCE = memoize(Spheres::new);

    static Spheres spheres() {
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

    Ball get(Color color) {
        return byColor.get().get(color);
    }

    Ball getHome(Color color) {
        return homeByColor.get().get(color);
    }
}
