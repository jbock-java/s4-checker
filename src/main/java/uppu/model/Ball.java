package uppu.model;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import uppu.engine.Mover;

public class Ball {

    private final Sphere sphere;

    private Timeline tl;

    private Ball(Sphere sphere) {
        this.sphere = sphere;
    }

    static class Builder {
        private final javafx.scene.paint.Color color;
        private final double radius;
        private final DrawMode drawMode;

        Builder(Color color, double radius, DrawMode drawMode) {
            this.color = color.awtColor();
            this.radius = radius;
            this.drawMode = drawMode;
        }

        Ball build() {
            return build(64);
        }

        Ball build(int divisions) {
            Sphere sphere = new Sphere(radius, divisions);
            sphere.setMaterial(new PhongMaterial(color));
            sphere.setDrawMode(drawMode);
            return new Ball(sphere);
        }
    }

    static Builder create(Color color, double radius, DrawMode drawMode) {
        return new Builder(color, radius, drawMode);
    }

    public void move(
            Mover mover,
            Point3D span,
            int seconds,
            Runnable onSuccess) {
        if (tl != null) {
            tl.stop();
            tl = null;
        }

        Point3D source = mover.source().homePoint();
        Point3D target = mover.destination().homePoint();

        if (mover.source().equals(mover.destination())) {
            setLocation(target);
            onSuccess.run();
            return;
        }

        DoubleProperty x = sphere.translateXProperty();
        DoubleProperty y = sphere.translateYProperty();
        DoubleProperty z = sphere.translateZProperty();

        int segments = 64;
        KeyFrame[] frames = new KeyFrame[segments + 1];
        float frac = 1.0f / segments;
        Point3D inc = target.subtract(source).multiply(frac);
        for (int i = 0; i <= segments; i++) {
            Point3D p = source.add(inc.multiply(i));
            double factor = Math.sin(Math.PI * frac * i);
            Point3D dd = p.add(span.multiply(factor * 0.25));
            frames[i] = new KeyFrame(Duration.seconds(seconds * frac * i),
                    new KeyValue(x, dd.getX(), Interpolator.LINEAR),
                    new KeyValue(y, dd.getY(), Interpolator.LINEAR),
                    new KeyValue(z, dd.getZ(), Interpolator.LINEAR));
        }

        tl = new Timeline(frames);
        tl.setCycleCount(1);
        tl.play();
        tl.setOnFinished(ev -> {
            tl = null;
            onSuccess.run();
        });
    }

    public void moveSimple(
            Mover mover,
            int seconds,
            Runnable onSuccess) {
        Point3D source = mover.source().homePoint();
        Point3D target = mover.destination().homePoint();

        if (mover.source().equals(mover.destination())) {
            setLocation(target);
            onSuccess.run();
            return;
        }

        DoubleProperty x = sphere.translateXProperty();
        DoubleProperty y = sphere.translateYProperty();
        DoubleProperty z = sphere.translateZProperty();

        tl = new Timeline(new KeyFrame(Duration.ZERO,
                new KeyValue(x, source.getX()),
                new KeyValue(y, source.getY()),
                new KeyValue(z, source.getZ())),
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(x, target.getX(), Interpolator.LINEAR),
                        new KeyValue(y, target.getY(), Interpolator.LINEAR),
                        new KeyValue(z, target.getZ(), Interpolator.LINEAR)));
        tl.setCycleCount(1);
        tl.play();
        tl.setOnFinished(ev -> {
            tl = null;
            onSuccess.run();
        });
    }

    public void moveCircle(
            Mover mover,
            Rotation rotation,
            double seconds,
            Runnable onSuccess,
            double angle) {

        if (mover.source().equals(mover.destination())) {
            setLocation(mover.destination().homePoint());
            onSuccess.run();
            return;
        }

        tl = new Timeline(getRotationTimeline(mover, rotation, angle, seconds));
        tl.setCycleCount(1);
        tl.play();
        tl.setOnFinished(ev -> {
            tl = null;
            onSuccess.run();
        });
    }

    private KeyFrame[] getRotationTimeline(
            Mover mover,
            Rotation rotation,
            double angle,
            double seconds) {
        Point3D source = mover.source().homePoint();
        int steps = 64;
        double angle_step = angle / steps;
        float time_step = (float) seconds / steps;
        DoubleProperty x = sphere.translateXProperty();
        DoubleProperty y = sphere.translateYProperty();
        DoubleProperty z = sphere.translateZProperty();
        KeyFrame[] frames = new KeyFrame[steps + 1];
        for (int i = 0; i <= steps; i++) {
            Point3D dd = rotation.apply(source, angle_step * i);
            frames[i] = new KeyFrame(Duration.seconds(time_step * i),
                    new KeyValue(x, dd.getX(), Interpolator.LINEAR),
                    new KeyValue(y, dd.getY(), Interpolator.LINEAR),
                    new KeyValue(z, dd.getZ(), Interpolator.LINEAR));
        }
        return frames;
    }

    public void setRunning(boolean running) {
        if (tl == null) {
            return;
        }
        if (running) {
            tl.play();
        } else {
            tl.pause();
        }
    }

    public void stop(boolean shred) {
        if (tl == null) {
            return;
        }
        tl.stop();
        if (shred) {
            tl = null;
        }
    }

    public Sphere sphere() {
        return sphere;
    }

    public void setLocation(Point3D target) {
        DoubleProperty x = sphere.translateXProperty();
        DoubleProperty y = sphere.translateYProperty();
        DoubleProperty z = sphere.translateZProperty();
        x.set(target.getX());
        y.set(target.getY());
        z.set(target.getZ());
    }
}
