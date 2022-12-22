package uppu;

import javafx.application.Platform;
import uppu.engine.Animation;
import uppu.model.ActionSequence;
import uppu.model.State;
import uppu.view.PermutationView;

import java.util.List;

public class Presenter {

    private final PermutationView view;
    private final CommandLine commandLine;
    private final State state;
    private final Animation animation;

    Presenter(
            PermutationView view,
            CommandLine commandLine,
            State state,
            Animation animation) {
        this.view = view;
        this.commandLine = commandLine;
        this.state = state;
        this.animation = animation;
    }

    public void run(List<ActionSequence> actions) {
        view.setOnActionSelected(animation::select);
        animation.setOnNext(view::setSelectedAction);
        Platform.runLater(() -> {
            setRunning(true);
            animation.setActions(actions);
            view.setActions(actions);
            actions.stream().findFirst().ifPresent(view::setSelectedAction);
        });
    }

    private void setRunning(boolean running) {
        animation.setRunning(running);
    }
}
