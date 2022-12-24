package uppu;

import javafx.application.Platform;
import uppu.model.ActionSequence;
import uppu.view.PermutationView;

import java.util.List;

public class Presenter {

    private final PermutationView view;
    private final CommandLine commandLine;
    private boolean running = true;
    private int current = 0;

    Presenter(
            PermutationView view,
            CommandLine commandLine) {
        this.view = view;
        this.commandLine = commandLine;
    }

    public void run(List<ActionSequence> actions) {
        view.setOnActionSelected(action -> {
            view.stop();
            current = actions.indexOf(action);
            view.setSelectedAction(action);
        });
        view.setOnAnimationFinished(() -> {
            int i = ++current;
            if (i < actions.size()) {
                view.setSelectedAction(actions.get(i));
            }
        });
        view.setOnPauseButtonClicked(() -> {
            running = !running;
            view.setRunning(running);
        });
        Platform.runLater(() -> {
            setRunning(true);
            view.setActions(actions);
            actions.stream().findFirst().ifPresent(view::setSelectedAction);
        });
    }

    private void setRunning(boolean running) {
        view.setRunning(running);
    }
}
