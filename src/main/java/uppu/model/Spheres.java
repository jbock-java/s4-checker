package uppu.model;

import java.util.function.Supplier;

import static uppu.util.Suppliers.memoize;

public final class Spheres {

    private final Ball redSphere = new Ball(Color.RED);
    private final Ball greenSphere = new Ball(Color.GREEN);
    private final Ball blueSphere = new Ball(Color.BLUE);
    private final Ball silverSphere = new Ball(Color.SILVER);

    private static final Supplier<Spheres> INSTANCE = memoize(Spheres::new);

    public static Spheres spheres() {
        return INSTANCE.get();
    }

    public Ball redSphere() {
        return redSphere;
    }

    public Ball greenSphere() {
        return greenSphere;
    }

    public Ball blueSphere() {
        return blueSphere;
    }

    public Ball silverSphere() {
        return silverSphere;
    }
}
