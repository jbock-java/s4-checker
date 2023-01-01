package uppu;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import uppu.model.ActionSequence;
import uppu.model.CommandSequence;
import uppu.model.State;
import uppu.view.InputView;
import uppu.view.PermutationView;

import java.util.List;
import java.util.function.Consumer;

import static uppu.util.Delay.runDelayed;

public class Presenter {

    private final PermutationView view;
    private final Consumer<List<CommandSequence>> onSave;
    private boolean running = true;
    private int current = 0;
    private PauseTransition wait;
    private List<ActionSequence> actions;

    public Presenter(
            PermutationView view,
            Consumer<List<CommandSequence>> onSave,
            List<ActionSequence> actions) {
        this.view = view;
        this.onSave = onSave;
        this.actions = actions;
    }

    public static Presenter create(
            PermutationView view,
            Consumer<List<CommandSequence>> onSave,
            List<CommandSequence> commands) {
        return new Presenter(view, onSave, State.create().getActions(commands));
    }

    public void run() {
        view.setOnActionSelected(action -> {
            stop();
            current = actions.indexOf(action);
            view.setSelectedAction(action);
            if (!running) {
                setRunning(false);
            }
        });
        view.setOnAnimationFinished(() -> {
            wait = runDelayed(400, () -> {
                wait = null;
                if (current < actions.size() - 1) {
                    current++;
                } else {
                    current = 0;
                }
                view.setSelectedAction(actions.get(current));
                setRunning(false);
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
        view.setOnLeft(() -> {
            view.stop();
            view.setSelectedAction(actions.get(current));
            if (!running) {
                setRunning(false);
            }
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
                            alert.show();
                        },
                        newCommands -> {
                            stop();
                            actions = State.create().getActions(newCommands);
                            view.setActions(actions);
                            onSave.accept(newCommands);
                            actions.stream().findFirst().ifPresent(view::setSelectedAction);
                            if (!actions.isEmpty()) {
                                current = 0;
                            }
                            setRunning(!actions.isEmpty());
                        });
                inputView.close();
            });
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
        this.running = running;
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
    }
}
