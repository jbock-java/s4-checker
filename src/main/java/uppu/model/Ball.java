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
            int seconds,
            Runnable onSuccess) {
        move(mover, mover.midPoint().normalize(), seconds, onSuccess);
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
            double factor = Math.sin(Math.PI * (1f / 6f + frac * i * 2f / 3f)) - 0.5f;
            Point3D dd = p.add(span.multiply(factor * HomePoints.FACE_RADIUS));
            frames[i] = new KeyFrame(Duration.seconds(seconds * frac * i),
                    new KeyValue(x, dd.getX(), Interpolator.LINEAR),
                    new KeyValue(y, dd.getY(), Interpolator.LINEAR),
                    new KeyValue(z, dd.getZ(), Interpolator.LINEAR));
        }

        tl = new Timeline(frames);
        tl.setCycleCount(1);
        tl.play();
        tl.setOnFinished(ev -> {
            onSuccess.run();
        });
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
