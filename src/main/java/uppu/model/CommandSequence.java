package uppu.model;

import io.parmigiano.Permutation;
import uppu.parse.Row;

import java.util.ArrayList;
import java.util.List;

import static uppu.model.Command.moveCommand;

public class CommandSequence {

    public static Result result(CommandSequence sequence, Permutation permutation, List<Colour> state) {
        return new Result(sequence, permutation, state, sequence.title);
    }

    public record Result(CommandSequence sequence, Permutation permutation, List<Colour> state, String title) {

        public Result title(String newTitle) {
            return new Result(sequence, permutation, state, newTitle);
        }
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
        Permutation product = Permutation.identity();
        for (int i = input.size() - 1; i >= 0; i--) {
            Permutation p = input.get(i);
            Command command = moveCommand(p);
            product = p.compose(product);
            abCommands.add(command);
            if (i > 0) {
                abCommands.add(Command.wait(15));
            }
        }
        return result(new CommandSequence(
                row.toString(current),
                abCommands), product, product.apply(Colour.getValues()));
    }

    public List<Command> commands() {
        return commands;
    }

    @Override
    public String toString() {
        return title;
    }
}
