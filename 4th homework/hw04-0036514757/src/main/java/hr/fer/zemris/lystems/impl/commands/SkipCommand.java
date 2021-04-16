package hr.fer.zemris.lystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lystems.impl.Command;
import hr.fer.zemris.lystems.impl.Context;
import hr.fer.zemris.lystems.impl.TurtleState;
import hr.fer.zemris.lystems.impl.Vector2D;

/**
 * Skip command is used for turtle to move to next point without leaving a trace.
 */
public class SkipCommand implements Command {
    private double step;

    public SkipCommand(double step) {
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

        currentState.setCurrentPosition(new Vector2D(newX, newY));

    }
}
