package hr.fer.zemris.lystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lystems.impl.Command;
import hr.fer.zemris.lystems.impl.Context;

/**
 * Pop command is used to pop the top state on context stack.
 */
public class PopCommand implements Command {
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.popState();
    }
}
