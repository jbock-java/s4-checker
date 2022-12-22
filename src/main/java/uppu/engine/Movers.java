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
        boolean result = false;
        for (Mover m : movers) {
            result |= m.move();
        }
        return result;
    }

    public void init() {
        for (Mover m : movers) {
            m.init();
        }
    }
}
