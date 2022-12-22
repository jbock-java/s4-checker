package uppu.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import uppu.model.Action;
import uppu.model.ActionSequence;
import uppu.view.PermutationView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Animation {

    private AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (onTimerTick()) {
                showState();
            }
        }
    };

    private final PermutationView view;
    private final List<ActionSequence> q = new ArrayList<>();
    private Consumer<ActionSequence> onNext = action -> {
    };
    private int current;

    private Animation(PermutationView view) {
        this.view = view;
    }

    public static Animation create(PermutationView view) {
        return new Animation(view);
    }

    private boolean onTimerTick() {
        ActionSequence actionSequence = peekFirst();
        if (actionSequence == null) {
            view.setTitle("");
            timer.stop();
            return false;
        }
        Action action = actionSequence.peekFirst();
        if (action == null) {
            current++;
            cleanCurrent();
            ActionSequence next = peekFirst();
            if (next != null) {
                onNext.accept(next);
            }
            return false;
        }
        boolean anyMove = action.move();
        if (!anyMove) {
            actionSequence.increment();
            return false;
        }
        return true;
    }

    private void showState() {
        ActionSequence actionSequence = peekFirst();
        if (actionSequence == null) {
            return;
        }
        Action action = actionSequence.peekFirst();
        if (action == null) {
            return;
        }
        GraphicsContext g = view.getGraphicsContext();
        action.show(g);
    }

    private void cleanCurrent() {
        ActionSequence actionSequence = peekFirst();
        if (actionSequence == null) {
            view.setTitle("");
            return;
        }
        view.setTitle(actionSequence.title());
        if (current == 0) {
            return;
        }
        actionSequence.init();
    }

    private ActionSequence peekFirst() {
        return get(current);
    }

    private ActionSequence get(int n) {
        if (n >= q.size()) {
            return null;
        }
        return q.get(n);
    }

    public void setRunning(boolean running) {
        if (running) {
            timer.start();
            return;
        }
        timer.stop();
    }

    public void select(ActionSequence action) {
        for (int i = 0; i < q.size(); i++) {
            ActionSequence qs = q.get(i);
            if (action == qs) {
                current = i;
                cleanCurrent();
            }
        }
    }

    public void setOnNext(Consumer<ActionSequence> onNext) {
        this.onNext = onNext;
    }

    public List<ActionSequence> getActions() {
        return q;
    }

    public void setActions(List<ActionSequence> actions) {
        q.clear();
        q.addAll(actions);
        actions.stream().findFirst().map(ActionSequence::title).ifPresent(view::setTitle);
    }
}
