package uppu.model;

import io.parmigiano.Permutation;
import uppu.parse.Row;

import java.util.ArrayList;
import java.util.List;

import static uppu.model.Command.command;

public class CommandSequence {

    public record Result(CommandSequence sequence, Permutation permutation) {
    }

    private final String title;
    private final List<Command> commands;

    private CommandSequence(
            String title,
            List<Command> commands) {
        this.title = title;
        this.commands = commands;
    }

    public static Result toSequence(
            Row row,
            Permutation current) {
        List<Permutation> input = row.permutations(current);
        List<Command> abCommands = new ArrayList<>(input.size() * 2 + 1);
        abCommands.add(Command.showState());
        abCommands.add(Command.wait(80));
        Permutation product = Permutation.identity();
        for (int i = input.size() - 1; i >= 0; i--) {
            Permutation p = input.get(i);
            Command command = command(p);
            product = p.compose(product);
            abCommands.add(command);
            if (i > 0) {
                abCommands.add(Command.wait(15));
            }
        }
        return new Result(new CommandSequence(
                row.toString(current),
                abCommands), product);
    }

    public List<Command> commands() {
        return commands;
    }

    public String title() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
