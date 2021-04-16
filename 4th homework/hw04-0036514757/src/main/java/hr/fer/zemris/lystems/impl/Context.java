package hr.fer.zemris.lystems.impl;

public class Context {
    private ObjectStack<TurtleState> stack;

    public Context() {
        this.stack = new ObjectStack<>();
    }

    /**
     * Returns current turtle state.
     *
     * @return current turtle state
     */
    public TurtleState getCurrentState() {
        return this.stack.peek();
    }

    /**
     * Pushes a new turtle state on top of the stack.
     *
     * @param state to be pushed
     */
    public void pushState(TurtleState state) {
        this.stack.push(state);
    }

    /**
     * Pops the top state in stack.
     */
    public void popState() {
        this.stack.pop();
    }
}
