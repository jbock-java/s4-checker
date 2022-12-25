package uppu.engine;

import javafx.geometry.Point3D;
import uppu.model.Color;

public record Path(
        Color source, // from which color-home
        Color destination // to which color-home
) {

    public Path normalize() {
        if (source.ordinal() <= destination.ordinal()) {
            return this;
        }
        return new Path(destination, source);
    }

    public Point3D midPoint() {
        return source.homePoint().add(destination.homePoint()).multiply(0.5d);
    }

    public boolean isZero() {
        return source == destination;
    }

    public Point3D arrow() {
        return destination.homePoint().subtract(source.homePoint());
    }
}
