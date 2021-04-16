package hr.fer.zemris.lystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lystems.impl.Command;
import hr.fer.zemris.lystems.impl.Context;

/**
 * Push command is used to push current state to context stack.
 */
public class PushCommand implements Command {

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.pushState(ctx.getCurrentState().copy());
    }
}
