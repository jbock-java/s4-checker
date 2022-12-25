package uppu.engine;

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
}
