package uppu.model;

import uppu.engine.Mover;

import java.util.List;

public sealed interface Action permits Action.MoveAction, Action.WaitAction {
    record MoveAction(List<Mover> movers) implements Action {

        public List<Mover> nonZeroMovers() {
            return movers.stream().filter(m -> !m.path().isZero()).toList();
        }

        public List<Mover> zeroMovers() {
            return movers.stream().filter(m -> m.path().isZero()).toList();
        }
    }

    record WaitAction(int millis) implements Action {
    }
}
