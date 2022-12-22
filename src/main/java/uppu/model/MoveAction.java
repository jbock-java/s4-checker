package uppu.model;

import javafx.scene.canvas.GraphicsContext;
import uppu.engine.Mover;
import uppu.engine.Movers;

import java.util.List;

public class MoveAction extends Action {

    private final State state;
    private final Movers movers;

    private MoveAction(State state, Movers movers) {
        this.state = state;
        this.movers = movers;
    }

    static MoveAction create(State state, List<Mover> movers) {
        return new MoveAction(state, Movers.create(movers));
    }

    @Override
    public boolean move() {
        return movers.move();
    }

    @Override
    public void show(GraphicsContext g) {
        show(g, state);
    }

    @Override
    String type() {
        return "MOVE";
    }

    @Override
    public void init() {
        movers.init();
    }
}
