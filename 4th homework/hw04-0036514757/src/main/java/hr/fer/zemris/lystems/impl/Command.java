package hr.fer.zemris.lystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * This interface models a command.
 */
public interface Command {
    void execute(Context ctx, Painter painter);
}
