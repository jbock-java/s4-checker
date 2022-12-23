package uppu.model;

import javafx.scene.canvas.GraphicsContext;
import uppu.engine.Mover;
import uppu.engine.Movers;

import java.util.List;

public class MoveAction extends Action {

    private final State state;
    private final List<Mover> movers;

    private MoveAction(State state, List<Mover> movers) {
        this.state = state;
        this.movers = movers;
    }

    static MoveAction create(State state, List<Mover> movers) {
        return new MoveAction(state, movers);
    }
}
