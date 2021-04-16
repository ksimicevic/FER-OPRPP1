package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * CalcModelImpl is an implementation of CalcModel interface. This is where all calculator logic happens.
 */
public class CalcModelImpl implements CalcModel {
    private boolean edible;
    private boolean positive;
    private String input;
    private double value;
    private String frozenValue;
    private Double activeOperand;
    private DoubleBinaryOperator pendingOperation;
    private List<CalcValueListener> listeners;

    public CalcModelImpl() {
        listeners = new LinkedList<>();
        edible = true;
        positive = true;
        input = "";
        value = 0.0;
        frozenValue = null;
        activeOperand = null;
        pendingOperation = null;
    }


    @Override
    public void addCalcValueListener(CalcValueListener l) {
        listeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        listeners.remove(l);
    }

    @Override
    public double getValue() {
        return positive ? value : -value;
    }

    @Override
    public void setValue(double value) {
        this.value = value;
        this.input = String.valueOf(value);
        this.edible = false;
        this.notifyAllListeners();
    }

    @Override
    public boolean isEditable() {
        return edible;
    }

    @Override
    public void clear() {
        this.value = 0.0;
        this.input = "";
        this.edible = true;
        this.notifyAllListeners();
    }

    @Override
    public void clearAll() {
        this.clear();
        this.activeOperand = null;
        this.pendingOperation = null;
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if (!edible) {
            throw new CalculatorInputException("Cannot swap signs right now.");
        }

        this.positive = !this.positive;
        this.notifyAllListeners();
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (!edible) {
            throw new CalculatorInputException("Cannot input a decimal point right now.");
        }

        if (input.indexOf(".") > 0) {
            throw new CalculatorInputException("There is already a decimal point.");
        }

        if (input.isEmpty()) {
            throw new CalculatorInputException("Input is empty");
        } else {
            input = input + ".";
        }

        this.notifyAllListeners();
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (!edible) {
            throw new CalculatorInputException("Cannot insert a digit right now");
        }

        this.frozenValue = null;

        if (digit > 0 && input.startsWith("0") && !input.contains(".")) {
            char[] chars = input.toCharArray();

            int index = 0;

            for (char c : chars) {
                if (c == '0') {
                    index++;
                } else {
                    break;
                }
            }

            input = input.substring(index);
        }

        if (input.startsWith("0") && !input.contains(".") && digit == 0) {
            return;
        }

        String newInput = input + digit;
        double newValue;

        try {
            newValue = Double.parseDouble(newInput);
            input = newInput;

            if (Double.isFinite(newValue)) {
                value = newValue;
            } else {
                throw new CalculatorInputException("The input is too big!");
            }
        } catch (NumberFormatException ignored) {
            throw new CalculatorInputException("Invalid input");
        }

        this.notifyAllListeners();
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperand != null;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (activeOperand == null) {
            throw new IllegalStateException("There's no current active operand");
        }
        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
    }

    @Override
    public void clearActiveOperand() {
        this.activeOperand = null;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        this.pendingOperation = op;
    }

    @Override
    public String toString() {
        if (hasFrozenValue()) {
            return frozenValue;
        } else if (this.input != null && !this.input.isEmpty()) {
            return positive ? input : value < 0 ? input: "-" + input;
        } else {
            return positive ? "0" : "-0";
        }
    }

    public void freezeValue(String value) {
        this.frozenValue = value;
    }

    public boolean hasFrozenValue() {
        return frozenValue != null;
    }

    public void notifyAllListeners() {
        for (CalcValueListener listener : listeners) {
            listener.valueChanged(this);
        }
    }
}
