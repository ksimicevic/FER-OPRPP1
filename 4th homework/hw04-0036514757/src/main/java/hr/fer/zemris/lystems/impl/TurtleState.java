package hr.fer.zemris.lystems.impl;

import java.awt.*;

/**
 * This class models a turtle state consiting of all necessary information about turtle.
 */
public class TurtleState {
    private Vector2D currentPosition;
    private Vector2D currentAngle;
    private Color currentColor;
    private double effectiveLength;

    public TurtleState(Vector2D currentPosition, Vector2D currentAngle, Color currentColor, double effectiveLength) {
        this.currentPosition = currentPosition;
        this.currentAngle = currentAngle;
        this.currentColor = currentColor;
        this.effectiveLength = effectiveLength;
    }

    public TurtleState copy() {
        return new TurtleState(currentPosition, currentAngle, currentColor, effectiveLength);
    }

    public Vector2D getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Vector2D currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Vector2D getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(Vector2D currentAngle) {
        this.currentAngle = currentAngle;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public double getEffectiveLength() {
        return effectiveLength;
    }

    public void setEffectiveLength(double effectiveLength) {
        this.effectiveLength = effectiveLength;
    }

}
