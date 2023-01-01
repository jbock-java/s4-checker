package uppu.test.game;

import io.parmigiano.Permutation;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import uppu.model.ActionSequence;
import uppu.model.Colour;
import uppu.model.State;
import uppu.view.PermutationView;

import java.util.List;

import static uppu.test.Month.APRIL;
import static uppu.test.Month.AUGUST;
import static uppu.test.Month.NOVEMBER;
import static uppu.test.Month.OCTOBER;

class GamePresenter {

    private final PermutationView view;
    private boolean running;

    private List<Colour> state = Colour.getValues();

    private static final List<Colour> GOAL = List.of(Colour.BLUE, Colour.RED, Colour.GREEN, Colour.SILVER);

    GamePresenter(PermutationView view) {
        this.view = view;
    }

    public void run() {
        view.setOnAnimationFinished(() -> {
            if (GOAL.equals(state)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Well done!", ButtonType.OK);
                alert.setOnHidden(evt -> Platform.exit());
                alert.show();
                view.setOnLeft(() -> {
                });
                view.setOnRight(() -> {
                });
            }
            running = false;
        });
        view.setOnLeft(() -> {
            if (running) {
                return;
            }
            view.stop();
            view.setSelectedAction(getSequence(NOVEMBER.permutation()));
            setRunning();
        });
        view.setOnRight(() -> {
            if (running) {
                return;
            }
            view.stop();
            view.setSelectedAction(getSequence(APRIL.permutation()));
            setRunning();
        });
        view.setOnD(() -> {
            if (running) {
                return;
            }
            view.stop();
            view.setSelectedAction(getSequence(OCTOBER.permutation()));
            setRunning();
        });
        view.setOnC(() -> {
            if (running) {
                return;
            }
            view.stop();
            view.setSelectedAction(getSequence(AUGUST.permutation()));
            setRunning();
        });
    }

    private ActionSequence getSequence(Permutation p) {
        State.ActionWithState moveAction = State.create().getMoveAction(p, state);
        this.state = moveAction.finalState();
        return new ActionSequence(List.of(moveAction.action()), "Use Left-Right arrow keys, and C and D keys.");
    }

    private void setRunning() {
        this.running = true;
        view.setRunning(true);
    }
}
