package uppu.test;

import org.junit.jupiter.api.Test;

public class PresenterTest {

    @Test
    void launch() {
        // Good?
        if (System.getenv("actuallyRun") == null) {
            return;
        }
        PresenterChecker.main(new String[0]);
    }
}
