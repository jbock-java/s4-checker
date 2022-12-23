package uppu.model;

public sealed interface Action permits MoveAction, WaitAction {
}
