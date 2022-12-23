package uppu.engine;

import java.util.List;

public class Movers {

    private final List<Mover> movers;

    private Movers(List<Mover> movers) {
        this.movers = movers;
    }

    public static Movers create(List<Mover> movers) {
        return new Movers(movers);
    }

    public boolean move() {
        return false;
    }

    public void init() {
    }
}
