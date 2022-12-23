package uppu.model;

import java.util.List;

public final class ActionSequence {

    private final List<Action> actions;
    private final String title;

    private int current;

    public ActionSequence(List<Action> actions, String title) {
        this.actions = actions;
        this.title = title;
    }

    public void init() {
        current = 0;
    }

    public Action peekFirst() {
        if (current >= actions.size()) {
            return null;
        }
        return actions.get(current);
    }

    public List<Action> actions() {
        return actions;
    }

    public void increment() {
        current++;
    }

    public String title() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
