package hr.fer.zemris.lystems.impl;

/**
 * This class represents a model of a 2D vector.
 */
public class Vector2D {
    private double x;
    private double y;

    /**
     * Creates a new 2D vector.
     *
     * @param x coordinate of vector
     * @param y coordinate of vector
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x component of vector.
     *
     * @return x component
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y component of vector.
     *
     * @return y component
     */
    public double getY() {
        return y;
    }

    /**
     * Adds another vector to this vector.
     *
     * @param offset vector to be added to this vector
     */
    public void add(Vector2D offset) {
        this.x += offset.x;
        this.y += offset.y;
    }

    /**
     * Returns new vector that is result of adding operation of this and given vector.
     *
     * @param offset vector that will be added to this vector to create the resulting vector
     * @return new vector that is the result of adding operation
     */
    public Vector2D added(Vector2D offset) {
        return new Vector2D(this.x + offset.x, this.y + offset.y);
    }

    /**
     * Rotates this vector anticlockwise around the origin by angle degrees.
     *
     * @param angle degrees that this vector will be rotated around the origin
     */
    public void rotate(double angle) {
        double x = this.x;
        double y = this.y;

        this.x = Math.cos(Math.toRadians(angle)) * x - Math.sin(Math.toRadians(angle)) * y;
        this.y = Math.sin(Math.toRadians(angle)) * x + Math.cos(Math.toRadians(angle)) * y;
    }

    /**
     * Rotates this vector anticlockwise around the origin by angle degrees and returns the resulting vector.
     *
     * @param angle degrees that this vector will be rotated around the origin
     * @return new vector that is the result of rotation
     */
    public Vector2D rotated(double angle) {
        double x = Math.cos(Math.toRadians(angle)) * this.x - Math.sin(Math.toRadians(angle)) * this.y;
        double y = Math.sin(Math.toRadians(angle)) * this.x + Math.cos(Math.toRadians(angle)) * this.y;

        return new Vector2D(x, y);
    }

    /**
     * Scales this vector by given amount.
     *
     * @param scaler
     */
    public void scale(double scaler) {
        this.x *= scaler;
        this.y *= scaler;
    }

    /**
     * Scales this vector by given amount and returns the result as new vector.
     *
     * @param scaler
     * @return new vector that is the result of scaling
     */
    public Vector2D scaled(double scaler) {
        return new Vector2D(this.x * scaler, this.y * scaler);
    }

    /**
     * Creates a new identical vector to this one.
     *
     * @return new copied vector
     */
    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    public static void main (String... args) {
        Vector2D v = new Vector2D(3.4, 1.2);

        v.rotate(Math.PI / 2);
        System.out.println(v.getX() + " " + v.getY());
    }
}
