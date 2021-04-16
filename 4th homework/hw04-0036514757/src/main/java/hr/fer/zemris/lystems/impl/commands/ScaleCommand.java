package hr.fer.zemris.lystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lystems.impl.Command;
import hr.fer.zemris.lystems.impl.Context;
import hr.fer.zemris.lystems.impl.TurtleState;

/**
 * Scale command is used to scale the effective length.
 */
public class ScaleCommand implements Command {
    private double scale;

    public ScaleCommand(double scale) {
        this.scale = scale;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        TurtleState currentState = ctx.getCurrentState();
        currentState.setEffectiveLength(currentState.getEffectiveLength() * scale);
    }
}
