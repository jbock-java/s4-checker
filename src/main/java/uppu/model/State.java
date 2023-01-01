package uppu.model;

import io.parmigiano.Permutation;
import uppu.engine.Mover;
import uppu.engine.Path;
import uppu.model.Command.MoveCommand;
import uppu.model.Command.WaitCommand;
import uppu.parse.Row;
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

    public List<CommandSequence.Result> getCommands(List<? extends Row> permutations) {
        Permutation current = Permutation.identity();
        List<CommandSequence.Result> result = new ArrayList<>();
        for (Row p : permutations) {
            CommandSequence.Result tmp = CommandSequence.toSequence(p, current);
            current = tmp.permutation().compose(current);
            result.add(new CommandSequence.Result(tmp.sequence(), current));
        }
        return result;
    }


    public List<ActionSequence> getActions(List<CommandSequence> sequences) {
        List<Colour> state = Colour.getValues();
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
            List<Colour> finalState) {
    }

    private ActionWithState getAction(
            List<Colour> state,
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
            List<Colour> state) {
        List<Mover> movers = new ArrayList<>();
        Colour[] newColors = new Colour[state.size()];
        for (int i = 0; i < state.size(); i++) {
            Colour color = state.get(i);
            int j = p.apply(i);
            newColors[j] = color;
            movers.add(Mover.create(color, new Path(Colour.get(i), Colour.get(j))));
        }
        return new ActionWithState(new Action.MoveAction(movers), List.of(newColors));
    }
}
