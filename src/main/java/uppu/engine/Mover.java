package uppu.engine;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import uppu.model.Ball;
import uppu.model.Colour;
import uppu.model.Rotation;

import static java.lang.Math.round;
import static java.lang.Math.toIntExact;
import static java.lang.System.arraycopy;

public final class Mover {

    public static final int ACC_FRAMES = 30;

    private final Path path;
    private final Point3D source;
    private final Point3D dest;

    private final Sphere sphere;
    private final Ball ball;

    private Mover(
            Path path,
            Point3D source,
            Point3D dest,
            Sphere sphere,
            Ball ball) {
        this.path = path;
        this.source = source;
        this.dest = dest;
        this.sphere = sphere;
        this.ball = ball;
    }

    public static Mover create(
            Colour color,
            Path path) {
        Ball ball = color.sphere();
        Point3D source = path.source().homePoint();
        Point3D dest = path.destination().homePoint();
        return new Mover(path, source, dest, ball.sphere(), ball);
    }

    public Ball ball() {
        return ball;
    }

    public Colour source() {
        return path.source();
    }

    public Path path() {
        return path;
    }

    public Colour destination() {
        return path.destination();
    }

    public Point3D midPoint() {
        return path.midPoint();
    }

    public Timeline move(
            Point3D span,
            int seconds,
            Runnable onSuccess) {

        if (path.isZero()) {
            ball.setLocation(dest);
            onSuccess.run();
            return new Timeline();
        }

        DoubleProperty x = sphere.translateXProperty();
        DoubleProperty y = sphere.translateYProperty();
        DoubleProperty z = sphere.translateZProperty();


        double[] steps = smoothInterval(1, seconds);
        float time_step = (float) seconds / steps.length;
        KeyFrame[] frames = new KeyFrame[steps.length];
        Point3D inc = dest.subtract(source);
        for (int i = 0; i < steps.length; i++) {
            Point3D p = source.add(inc.multiply(steps[i]));
            double factor = Math.sin(Math.PI * steps[i]);
            Point3D dd = p.add(span.multiply(factor * 0.25));
            frames[i] = new KeyFrame(Duration.seconds(time_step * i),
                    new KeyValue(x, dd.getX(), Interpolator.LINEAR),
                    new KeyValue(y, dd.getY(), Interpolator.LINEAR),
                    new KeyValue(z, dd.getZ(), Interpolator.LINEAR));
        }

        Timeline tl = new Timeline(frames);

        tl.setCycleCount(1);
        tl.setOnFinished(ev -> {
            onSuccess.run();
        });
        tl.play();
        return tl;
    }

    public Timeline moveSimple(
            int seconds,
            Runnable onSuccess) {

        if (path.isZero()) {
            ball.setLocation(dest);
            onSuccess.run();
            return new Timeline();
        }

        DoubleProperty x = sphere.translateXProperty();
        DoubleProperty y = sphere.translateYProperty();
        DoubleProperty z = sphere.translateZProperty();

        double[] steps = smoothInterval(1, seconds);
        float time_step = (float) seconds / steps.length;
        KeyFrame[] frames = new KeyFrame[steps.length];
        Point3D inc = dest.subtract(source);
        for (int i = 0; i < steps.length; i++) {
            Point3D dd = source.add(inc.multiply(steps[i]));
            frames[i] = new KeyFrame(Duration.seconds(time_step * i),
                    new KeyValue(x, dd.getX(), Interpolator.LINEAR),
                    new KeyValue(y, dd.getY(), Interpolator.LINEAR),
                    new KeyValue(z, dd.getZ(), Interpolator.LINEAR));
        }

        Timeline tl = new Timeline(frames);

        tl.setCycleCount(1);
        tl.setOnFinished(ev -> {
            onSuccess.run();
        });
        tl.play();
        return tl;
    }

    public Timeline moveCircle(
            Rotation rotation,
            double seconds,
            double angle,
            Runnable onSuccess) {

        if (path.isZero()) {
            ball.setLocation(destination().homePoint());
            onSuccess.run();
            return new Timeline();
        }

        Timeline tl = new Timeline(getRotationTimeline(rotation, angle, seconds));
        tl.setCycleCount(1);
        tl.setOnFinished(ev -> {
            onSuccess.run();
        });
        tl.play();
        return tl;
    }

    private KeyFrame[] getRotationTimeline(
            Rotation rotation,
            double angle,
            double seconds) {
        DoubleProperty x = sphere.translateXProperty();
        DoubleProperty y = sphere.translateYProperty();
        DoubleProperty z = sphere.translateZProperty();
        double[] angleSteps = smoothInterval(angle, seconds);
        float time_step = (float) seconds / angleSteps.length;
        KeyFrame[] frames = new KeyFrame[angleSteps.length];
        for (int i = 0; i < angleSteps.length; i++) {
            Point3D dd = rotation.apply(source, angleSteps[i]);
            frames[i] = new KeyFrame(Duration.seconds(time_step * i),
                    new KeyValue(x, dd.getX(), Interpolator.LINEAR),
                    new KeyValue(y, dd.getY(), Interpolator.LINEAR),
                    new KeyValue(z, dd.getZ(), Interpolator.LINEAR));
        }
        return frames;
    }

    public static double[] smoothInterval(
            double length,
            double seconds) {
        double topSpeed = length / (seconds * 20);
        double[] acc = accelerate(topSpeed);
        double acc_n = acc[acc.length - 1];
        double dec_0 = length - acc_n;
        double[] dec = decelerate(dec_0, topSpeed);
        double[] cruise = createCruise(topSpeed, acc_n, dec_0);
        double[] result = new double[acc.length + cruise.length + dec.length];
        arraycopy(acc, 0, result, 0, acc.length);
        arraycopy(cruise, 0, result, acc.length, cruise.length);
        arraycopy(dec, 0, result, acc.length + cruise.length, dec.length);
        return result;
    }

    private static double[] createCruise(
            double topSpeed,
            double acc_n,
            double dec_0) {
        double dist = dec_0 - acc_n;
        int n = toIntExact(round(dist / topSpeed));
        double speed = dist / n;
        double[] cruise = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            cruise[i] = acc_n + (speed * (i + 1));
        }
        return cruise;
    }

    static double[] accelerate(double topSpeed) {
        double[] result = new double[ACC_FRAMES];
        double inc = topSpeed / (result.length - 1);
        for (int i = 1; i < result.length; i++) {
            result[i] = result[i - 1] + (inc * i);
        }
        return result;
    }

    static double[] decelerate(double initial, double topSpeed) {
        double[] result = new double[ACC_FRAMES];
        double inc = topSpeed / (result.length - 1);
        result[0] = initial;
        for (int i = 1; i < result.length; i++) {
            result[i] = result[i - 1] + (inc * (result.length - i));
        }
        return result;
    }
}
