package uppu.model;

import javafx.scene.canvas.GraphicsContext;
import uppu.engine.Mover;

public abstract class Action {

    public static final int BALL_SIZE = (int) (50 * HomePoints.SCALE);
    public static final int GLOW_SIZE = 3;

    private static final OffsetEllipse[] ELLIPSES = new OffsetEllipse[Mover.STEPS];

    static {
        float v = (BALL_SIZE - BALL_SIZE / 1.5f) / 60f;
        float vglow = BALL_SIZE / 360.0f;
        for (int i = 0; i < ELLIPSES.length; i++) {
            float ballSize = BALL_SIZE - (i * v);
            ELLIPSES[i] = new OffsetEllipse(
                    ballSize,
                    ballSize + 2 * GLOW_SIZE,
                    vglow * i);
        }
    }

    public abstract boolean move();

    public abstract void show(GraphicsContext g);

    abstract String type();

    final void show(GraphicsContext g, State state) {
        Quadruple quadruple = state.quadruple();
        quadruple.clearRect(g);
        Color[] colors = quadruple.colors();
        for (Color color : colors) {
            OffsetEllipse offsetEllipse = ellipse(quadruple.getZ(color));
            float x = quadruple.getX(color) + quadruple.getOffsetX() + offsetEllipse.offset;
            float y = quadruple.getY(color) + quadruple.getOffsetY() + offsetEllipse.offset;
            float glowx = x - GLOW_SIZE;
            float glowy = y - GLOW_SIZE;
            g.setFill(color.glowColor());
            g.fillOval(glowx, glowy, offsetEllipse.glowSize, offsetEllipse.glowSize);
            g.setFill(color.awtColor());
            g.fillOval(x, y, offsetEllipse.ballSize, offsetEllipse.ballSize);
        }
        for (int i = 0; i < colors.length; i++) {
            g.setFill(colors[i].awtColor());
            state.homePoints().get(i).paintHome(g, quadruple);
        }
    }

    record OffsetEllipse(
            float ballSize,
            float glowSize,
            float offset) {
    }

    static OffsetEllipse ellipse(float z) {
        int bucket = Math.round((z - 1f) * 120);
        if (bucket >= 60) {
            bucket = 59;
        }
        return ELLIPSES[bucket];
    }

    public abstract void init();

    @Override
    public final String toString() {
        return type();
    }
}
