package uppu.model;

import uppu.engine.Mover;

import java.util.List;

public sealed interface Action permits Action.MoveAction, Action.WaitAction {
    record MoveAction(List<Mover> movers) implements Action {
    }

    record WaitAction(int millis) implements Action {
    }
}
