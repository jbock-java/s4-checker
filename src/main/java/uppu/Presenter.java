package uppu;

import javafx.application.Platform;
import uppu.model.ActionSequence;
import uppu.view.PermutationView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Presenter {

    private final PermutationView view;
    private final CommandLine commandLine;

    Presenter(
            PermutationView view,
            CommandLine commandLine) {
        this.view = view;
        this.commandLine = commandLine;
    }

    public void run(List<ActionSequence> actions) {
        AtomicInteger current = new AtomicInteger(0);
        view.setOnActionSelected(action -> {
//            int index = actions.indexOf(action);
//            current.set(index);
//            animation.select(action);
        });
        view.setOnAnimationFinished(() -> {
            int index = current.incrementAndGet();
            if (index < actions.size()) {
                view.setSelectedAction(actions.get(index));
            }
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
