package uppu.util;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public final class Delay {

    public static void runDelayed(long millis, Runnable r) {
        PauseTransition wait = new PauseTransition(Duration.millis(millis));
        wait.setOnFinished(e -> r.run());
        wait.play();
    }
    
    private Delay() {
    }
}
