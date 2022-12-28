package uppu.model;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;

public class Ball {

    private final Sphere sphere;

    private Ball(Sphere sphere) {
        this.sphere = sphere;
    }

    static class Builder {
        private final Color color;
        private final double radius;
        private final DrawMode drawMode;

        Builder(Colour color, double radius, DrawMode drawMode) {
            this.color = color.color();
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

    static Builder create(Colour color, double radius, DrawMode drawMode) {
        return new Builder(color, radius, drawMode);
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
