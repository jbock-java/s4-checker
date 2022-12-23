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

public class Ball {

    private final Sphere sphere = new Sphere(1.5f);

    Ball(Color color) {
        this.sphere.setMaterial(new PhongMaterial(color.awtColor()));
        this.sphere.setDrawMode(DrawMode.FILL);
    }

    public void move(
            Point3D source,
            Point3D target,
            int seconds,
            Runnable onSuccess) {

        DoubleProperty x = sphere.translateXProperty();
        DoubleProperty y = sphere.translateYProperty();
        DoubleProperty z = sphere.translateZProperty();

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
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
            onSuccess.run();
        });
    }

    public Sphere sphere() {
        return sphere;
    }
}
