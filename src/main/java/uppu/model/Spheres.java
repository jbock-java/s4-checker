package uppu.model;

public final class Spheres {

    private final Ball redSphere = new Ball(Color.RED);
    private final Ball greenSphere = new Ball(Color.GREEN);
    private final Ball blueSphere = new Ball(Color.BLUE);
    private final Ball silverSphere = new Ball(Color.SILVER);

    private static Spheres INSTANCE;

    public static Spheres spheres() {
        if (INSTANCE == null) {
            INSTANCE = new Spheres();
        }
        return INSTANCE;
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
