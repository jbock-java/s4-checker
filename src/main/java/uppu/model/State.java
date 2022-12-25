package uppu.model;

import io.parmigiano.Permutation;
import javafx.geometry.Point3D;
import uppu.engine.Mover;
import uppu.engine.Path;
import uppu.model.Command.MoveCommand;
import uppu.model.Command.WaitCommand;
import uppu.util.Suppliers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class State {

    private static final Supplier<State> INSTANCE = Suppliers.memoize(State::new);

    private State() {
    }

    public static State create() {
        return INSTANCE.get();
    }

    public List<ActionSequence> getActions(List<CommandSequence> sequences) {
        List<Color> state = Color.getValues();
        List<ActionSequence> actionSequences = new ArrayList<>();
        for (CommandSequence sequence : sequences) {
            List<Action> actions = new ArrayList<>(sequence.commands().size());
            for (Command command : sequence.commands()) {
                ActionWithState action = getAction(state, command);
                actions.add(action.action);
                state = action.finalState;
            }
            actionSequences.add(new ActionSequence(actions, sequence.title()));
        }
        return actionSequences;
    }

    private record ActionWithState(
            Action action,
            List<Color> finalState) {
    }

    private ActionWithState getAction(
            List<Color> state,
            Command command) {
        if (command instanceof MoveCommand) {
            return getMoveAction(((MoveCommand) command).permutation(), state);
        }
        if (command instanceof WaitCommand) {
            return new ActionWithState(new Action.WaitAction(((WaitCommand) command).cycles()), state);
        }
        throw new IllegalArgumentException();
    }

    private ActionWithState getMoveAction(
            Permutation p,
            List<Color> state) {
        List<Mover> movers = new ArrayList<>();
        Color[] newColors = new Color[state.size()];
        for (int i = 0; i < state.size(); i++) {
            Color color = state.get(i);
            int j = p.apply(i);
            newColors[j] = color;
            movers.add(new Mover(color, new Path(Color.get(i), Color.get(j))));
        }
        return new ActionWithState(new Action.MoveAction(movers), List.of(newColors));
    }
}
