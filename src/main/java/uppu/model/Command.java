package uppu.model;

import io.parmigiano.Permutation;

public sealed interface Command permits Command.MoveCommand, Command.WaitCommand {

    MoveCommand SHOW_STATE = new MoveCommand(Permutation.identity());

    static Command command(Permutation left) {
        return new MoveCommand(left);
    }

    static Command showState() {
        return SHOW_STATE;
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
