package uppu.model;

import javafx.scene.shape.DrawMode;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static uppu.util.Suppliers.memoize;

final class Spheres {

    private final Ball redSphere = Ball.create(Colour.RED, 0.24f, DrawMode.FILL).build();
    private final Ball greenSphere = Ball.create(Colour.GREEN, 0.24f, DrawMode.FILL).build();
    private final Ball blueSphere = Ball.create(Colour.BLUE, 0.24f, DrawMode.FILL).build();
    private final Ball silverSphere = Ball.create(Colour.SILVER, 0.24f, DrawMode.FILL).build();
    private final Ball redHome = Ball.create(Colour.RED, 0.4f, DrawMode.LINE).build(32);
    private final Ball greenHome = Ball.create(Colour.GREEN, 0.4f, DrawMode.LINE).build(32);
    private final Ball blueHome = Ball.create(Colour.BLUE, 0.4f, DrawMode.LINE).build(32);
    private final Ball silverHome = Ball.create(Colour.SILVER, 0.4f, DrawMode.LINE).build(32);

    private static final Supplier<Spheres> INSTANCE = memoize(Spheres::new);

    static Spheres spheres() {
        return INSTANCE.get();
    }

    private final Supplier<Map<Colour, Ball>> byColor = memoize(() -> {
        EnumMap<Colour, Ball> m = new EnumMap<>(Colour.class);
        m.put(Colour.GREEN, greenSphere);
        m.put(Colour.RED, redSphere);
        m.put(Colour.BLUE, blueSphere);
        m.put(Colour.SILVER, silverSphere);
        return m;
    });

    private final Supplier<Map<Colour, Ball>> homeByColor = memoize(() -> {
        EnumMap<Colour, Ball> m = new EnumMap<>(Colour.class);
        m.put(Colour.GREEN, greenHome);
        m.put(Colour.RED, redHome);
        m.put(Colour.BLUE, blueHome);
        m.put(Colour.SILVER, silverHome);
        return m;
    });

    Ball get(Colour color) {
        return byColor.get().get(color);
    }

    Ball getHome(Colour color) {
        return homeByColor.get().get(color);
    }
}
