package uppu.model;

import io.parmigiano.Permutation;

public sealed interface Command permits Command.MoveCommand, Command.WaitCommand {

    static Command moveCommand(Permutation p) {
        return new MoveCommand(p);
    }

    static Command wait(int cycles) {
        return new WaitCommand(cycles);
    }

    record MoveCommand(Permutation permutation) implements Command {

        @Override
        public String toString() {
            return "MOVE " + permutation;
        }
    }

    record WaitCommand(int cycles) implements Command {

        @Override
        public String toString() {
            return "WAIT " + cycles;
        }
    }
}
