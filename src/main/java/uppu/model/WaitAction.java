package uppu.model;

public final class WaitAction extends Action {

    private final int cyclesInit;
    private int cycles;

    private WaitAction(int cycles) {
        this.cyclesInit = cycles;
        this.cycles = cycles;
    }

    static WaitAction create(int cycles) {
        return new WaitAction(cycles);
    }
}
