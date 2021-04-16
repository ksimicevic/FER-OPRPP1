package hr.fer.zemris.lystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lystems.impl.Command;
import hr.fer.zemris.lystems.impl.Context;
import hr.fer.zemris.lystems.impl.TurtleState;

/**
 * Rotate command is used to rotate the turtle.
 */
public class RotateCommand implements Command {
    private double angle;

    public RotateCommand(double angle) {
        this.angle = angle;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        TurtleState currentState = ctx.getCurrentState();
        currentState.setCurrentAngle(currentState.getCurrentAngle().rotated(angle));
    }
}
