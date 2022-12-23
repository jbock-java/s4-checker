package uppu.model;

import javafx.scene.canvas.GraphicsContext;

public abstract class Action {

    public static final int BALL_SIZE = (int) (50 * HomePoints.SCALE);
    public static final int GLOW_SIZE = 3;

    private static final OffsetEllipse ELLIPSE = createEllipse();

    static OffsetEllipse createEllipse() {
        return new OffsetEllipse(
                (float) BALL_SIZE,
                (float) BALL_SIZE + 2 * GLOW_SIZE);
    }

    public abstract boolean move();

    public abstract void show(GraphicsContext g);

    abstract String type();

    final void show(GraphicsContext g, State state) {
        Quadruple quadruple = state.quadruple();
        quadruple.clearRect(g);
        Color[] colors = quadruple.colors();
        for (Color color : colors) {
            float x = quadruple.getX(color) + quadruple.getOffsetX();
            float y = quadruple.getY(color) + quadruple.getOffsetY();
            float glowx = x - GLOW_SIZE;
            float glowy = y - GLOW_SIZE;
            g.setFill(color.glowColor());
            g.fillOval(glowx, glowy, ELLIPSE.glowSize, ELLIPSE.glowSize);
            g.setFill(color.awtColor());
            g.fillOval(x, y, ELLIPSE.ballSize, ELLIPSE.ballSize);
        }
    }

    record OffsetEllipse(
            float ballSize,
            float glowSize) {
    }

    public abstract void init();

    @Override
    public final String toString() {
        return type();
    }
}
