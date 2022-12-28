package uppu;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private List<ActionSequence> actions;

    Presenter(
            PermutationView view,
            CommandLine commandLine,
            List<ActionSequence> actions) {
        this.view = view;
        this.commandLine = commandLine;
        this.actions = actions;
    }

    public void run() {
        view.setOnActionSelected(action -> {
            stop();
            current = actions.indexOf(action);
            view.setSelectedAction(action);
        });
        view.setOnAnimationFinished(() -> {
            wait = runDelayed(400, () -> {
                wait = null;
                if (current < actions.size() - 1) {
                    current++;
                    view.setSelectedAction(actions.get(current));
                    setRunning(false);
                }
                wait = runDelayed(1600, () -> {
                    wait = null;
                    setRunning(true);
                });
            });
        });
        view.setOnPauseButtonClicked(() -> {
            running = !running;
            setRunning(running);
        });
        view.setOnBack(() -> {
            view.stop();
            view.setSelectedAction(actions.get(current));
        });
        view.setOnEditButtonClicked(() -> {
            setRunning(false);
            InputView inputView = InputView.create();
            inputView.setContent(actions);
            inputView.setOnCancel(() -> {
                setRunning(true);
            });
            inputView.setOnSave(lines -> {
                S4Checker.readLines(lines).ifLeftOrElse(
                        error -> {
                            Alert alert = new Alert(AlertType.ERROR, error, ButtonType.OK);
                            alert.showAndWait();
                        },
                        newCommands -> {
                            stop();
                            actions = State.create().getActions(newCommands);
                            view.setActions(actions);
                            writeToFile(actions);
                            actions.stream().findFirst().ifPresent(view::setSelectedAction);
                            if (!actions.isEmpty()) {
                                current = 0;
                            }
                            setRunning(!actions.isEmpty());
                        });
                inputView.close();
            });
        });
        wait = runDelayed(0, () -> {
            wait = null;
            view.setHomesVisible(false);
        });
        wait = runDelayed(2000, () -> {
            wait = null;
            setRunning(true);
            Platform.runLater(() -> {
                view.setActions(actions);
                actions.stream().findFirst().ifPresent(view::setSelectedAction);
            });
        });
    }

    private void setRunning(boolean running) {
        view.setRunning(running);
        if (wait != null && running) {
            wait.play();
        }
        if (wait != null && !running) {
            wait.pause();
        }
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
            Alert alert = new Alert(AlertType.ERROR, e.toString(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}
