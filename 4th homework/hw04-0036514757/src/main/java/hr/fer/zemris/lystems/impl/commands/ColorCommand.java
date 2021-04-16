package hr.fer.zemris.lystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lystems.impl.Command;
import hr.fer.zemris.lystems.impl.Context;
import hr.fer.zemris.lystems.impl.TurtleState;

import java.awt.*;

/**
 * Color command is used to set a new color.
 */
public class ColorCommand implements Command {
    private Color color;

    public ColorCommand(Color color) {
        this.color = color;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        TurtleState currentState = ctx.getCurrentState();
        currentState.setCurrentColor(color);
    }
}
