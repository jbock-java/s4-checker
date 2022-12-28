package uppu.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uppu.engine.Mover.ACC_FRAMES;

class MoverTest {

    @Test
    void testMaxAcc() {
        double angle = Math.PI;
        double seconds = 4.2;
        double topSpeed = angle / (seconds * 20);
        double[] result = Mover.angleSteps(angle, seconds);
        checkAcc(result, topSpeed);
    }

    @Test
    void testMaxAcc2() {
        double angle = Math.PI * (2f / 3f);
        double seconds = 3;
        double topSpeed = angle / (seconds * 20);
        double[] result = Mover.angleSteps(angle, seconds);
        checkAcc(result, topSpeed);
    }

    private void checkAcc(
            double[] result,
            double topSpeed) {
        double accLimit = (topSpeed / ACC_FRAMES) * 1.05;
        double prevDiff = Math.abs(result[1] - result[0]);
        for (int i = 2; i < result.length; i++) {
            double diff = Math.abs(result[i] - result[i - 1]);
            double acc = Math.abs(prevDiff - diff);
            assertTrue(acc < accLimit,
                    "i: " + i +
                            ", acc: " + acc +
                            ", diff: " + diff +
                            ", max: " + accLimit);
            prevDiff = diff;
        }
    }
}