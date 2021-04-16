package hr.fer.zemris.lystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lystems.impl.Command;
import hr.fer.zemris.lystems.impl.Context;
import hr.fer.zemris.lystems.impl.TurtleState;
import hr.fer.zemris.lystems.impl.Vector2D;

/**
 * Draw command is used to draw the next turtle movement.
 */
public class DrawCommand implements Command {
    private double step;

    public DrawCommand(double step) {
        this.step = step;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        TurtleState currentState = ctx.getCurrentState();
        Vector2D currentPosition = currentState.getCurrentPosition();
        Vector2D angle = currentState.getCurrentAngle();

        double currentX = currentPosition.getX();
        double currentY = currentPosition.getY();

        double angleX = angle.getX();
        double angleY = angle.getY();

        double effectiveLength = currentState.getEffectiveLength();

        double newX = currentX + effectiveLength * step * angleX;
        double newY = currentY + effectiveLength * step * angleY;

        painter.drawLine(currentX, currentY, newX, newY, currentState.getCurrentColor(), 1f);

        currentState.setCurrentPosition(new Vector2D(newX, newY));
    }
}
