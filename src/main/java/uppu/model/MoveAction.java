package uppu.model;

import uppu.engine.Mover;

import java.util.List;

public record MoveAction(List<Mover> movers) implements Action {
}
