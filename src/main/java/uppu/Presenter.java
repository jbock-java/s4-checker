package uppu;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import uppu.model.ActionSequence;
import uppu.model.State;
import uppu.view.InputView;
import uppu.view.PermutationView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static uppu.util.Delay.runDelayed;

public class Presenter {

    private final PermutationView view;
    private final CommandLine commandLine;
    private boolean running = true;
    private int current = 0;
    private PauseTransition wait;

    Presenter(
            PermutationView view,
            CommandLine commandLine) {
        this.view = view;
        this.commandLine = commandLine;
    }

    public void run(List<ActionSequence> actions) {
        view.setOnActionSelected(action -> {
            stop();
            current = actions.indexOf(action);
            view.setSelectedAction(action);
        });
        view.setOnAnimationFinished(() -> {
            view.stop();
            view.setHomesVisible(true);
            wait = runDelayed(5000, () -> {
                if (current < actions.size() - 1) {
                    current++;
                    view.setHomesVisible(false);
                    view.setSelectedAction(actions.get(current));
                }
            });
        });
        view.setOnPauseButtonClicked(() -> {
            running = !running;
            view.setRunning(running);
        });
        view.setOnEditButtonClicked(() -> {
            stop();
            InputView inputView = InputView.create();
            inputView.setContent(actions);
            inputView.setOnSave(lines -> {
                S4Checker.readLines(lines).ifLeftOrElse(
                        error -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
                            alert.showAndWait();
                        },
                        newCommands -> {
                            List<ActionSequence> newActions = State.create().getActions(newCommands);
                            view.setActions(newActions);
//                            writeToFile(newActions);
                            newActions.stream().findFirst().ifPresent(view::setSelectedAction);
                            view.setRunning(true);
                        });
                inputView.close();
            });
        });
        runDelayed(2000, () -> {
            view.setHomesVisible(true);
            view.setRunning(true);
            Platform.runLater(() -> {
                view.setHomesVisible(false);
                view.setActions(actions);
                actions.stream().findFirst().ifPresent(view::setSelectedAction);
            });
        });
    }

    private void stop() {
        if (wait != null) {
            wait.stop();
            wait = null;
        }
        view.stop();
        view.setHomesVisible(false);
    }

    private void writeToFile(List<ActionSequence> newActions) {
        try {
            List<String> lines = newActions.stream().map(ActionSequence::toString).toList();
            Path p = commandLine.input().toPath();
            Files.write(p, lines, StandardOpenOption.TRUNCATE_EXISTING);
            Files.writeString(p, System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}
