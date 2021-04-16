package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Calculator class consisting of all GUI related things needed for a functioning calculator.
 */
public class Calculator extends JFrame {
    private CalcModelImpl calculator;
    private Stack<Double> stack;
    private boolean inverse;
    private HashMap<String, String> invertedNames;
    private HashMap<String, String> notInvertedNames;

    public Calculator() {
        calculator = new CalcModelImpl();
        stack = new Stack<>();
        invertedNames = new HashMap<>();
        notInvertedNames = new HashMap<>();
        inverse = false;
        initGUI();
    }

    /**
     * Method for all GUI related things.
     */
    private void initGUI() {
        Container container = getContentPane();

        CalcLayout layout = new CalcLayout(2);
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.BLUE);

        JLabel display = new JLabel(calculator.toString());
        display.setOpaque(true);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(display.getFont().deriveFont(30f));
        display.setBackground(Color.CYAN);
        display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        calculator.addCalcValueListener(model -> display.setText(model.toString()));
        panel.add(display, new RCPosition(1, 1));

        panel.add(buttonDesign(new JButton("0"), e -> calculator.insertDigit(0)), new RCPosition(5, 3));
        panel.add(buttonDesign(new JButton("1"), e -> calculator.insertDigit(1)), new RCPosition(4, 3));
        panel.add(buttonDesign(new JButton("2"), e -> calculator.insertDigit(2)), new RCPosition(4, 4));
        panel.add(buttonDesign(new JButton("3"), e -> calculator.insertDigit(3)), new RCPosition(4, 5));
        panel.add(buttonDesign(new JButton("4"), e -> calculator.insertDigit(4)), new RCPosition(3, 3));
        panel.add(buttonDesign(new JButton("5"), e -> calculator.insertDigit(5)), new RCPosition(3, 4));
        panel.add(buttonDesign(new JButton("6"), e -> calculator.insertDigit(6)), new RCPosition(3, 5));
        panel.add(buttonDesign(new JButton("7"), e -> calculator.insertDigit(7)), new RCPosition(2, 3));
        panel.add(buttonDesign(new JButton("8"), e -> calculator.insertDigit(8)), new RCPosition(2, 4));
        panel.add(buttonDesign(new JButton("9"), e -> calculator.insertDigit(9)), new RCPosition(2, 5));

        panel.add(buttonDesign(new JButton("="), equalsListener), new RCPosition(1, 6));
        panel.add(buttonDesign(new JButton("clr"), e -> calculator.clear()), new RCPosition(1, 7));
        panel.add(buttonDesign(new JButton("reset"), e -> calculator.clearAll()), new RCPosition(2, 7));
        panel.add(buttonDesign(new JButton("+/-"), e -> calculator.swapSign()), new RCPosition(5, 4));
        panel.add(buttonDesign(new JButton("."), e -> calculator.insertDecimalPoint()), new RCPosition(5, 5));

        panel.add(buttonDesign(new JButton("1/x"), listenerUnaryFactory(d -> 1.0 / d, null)), new RCPosition(2, 1));
        panel.add(buttonDesign(new JButton("sin"), listenerUnaryFactory(Math::sin, Math::asin), "arcsin"), new RCPosition(2, 2));
        panel.add(buttonDesign(new JButton("cos"), listenerUnaryFactory(Math::cos, Math::acos), "arccos"), new RCPosition(3, 2));
        panel.add(buttonDesign(new JButton("tan"), listenerUnaryFactory(Math::tan, Math::atan), "arctan"), new RCPosition(4, 2));
        panel.add(buttonDesign(new JButton("ctg"), listenerUnaryFactory(d -> 1.0 / Math.tan(d), d -> Math.PI / 2 - Math.atan(d)), "arcctg"), new RCPosition(5, 2));

        panel.add(buttonDesign(new JButton("log"), listenerUnaryFactory(Math::log10, d -> Math.pow(10, d)), "10^x"), new RCPosition(3, 1));
        panel.add(buttonDesign(new JButton("ln"), listenerUnaryFactory(Math::log, d -> Math.pow(Math.E, d)), "e^x"), new RCPosition(4, 1));

        panel.add(buttonDesign(new JButton("push"), e -> stack.push(calculator.getValue())), new RCPosition(3, 7));
        panel.add(buttonDesign(new JButton("pop"), popListener), new RCPosition(4, 7));

        panel.add(buttonDesign(new JButton("+"), listenerBinaryFactory(Double::sum, null)), new RCPosition(5, 6));
        panel.add(buttonDesign(new JButton("x^n"), listenerBinaryFactory(Math::pow, (x, n) -> Math.pow(x, 1 / n)), "x^(1/n)"), new RCPosition(5, 1));
        panel.add(buttonDesign(new JButton("/"), listenerBinaryFactory((l, r) -> l / r, null)), new RCPosition(2, 6));
        panel.add(buttonDesign(new JButton("*"), listenerBinaryFactory((l, r) -> l * r, null)), new RCPosition(3, 6));
        panel.add(buttonDesign(new JButton("-"), listenerBinaryFactory((l, r) -> l - r, null)), new RCPosition(4, 6));

        JCheckBox inverseCBox = new JCheckBox("Inv");
        inverseCBox.addItemListener(e -> {
            this.inverse = !inverse;
            invertNames(layout.getElements());
        });

        panel.add(inverseCBox, new RCPosition(5, 7));

        container.add(panel);

    }

    private final ActionListener popListener = e -> {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        } else {
            calculator.setValue(stack.pop());
        }
    };


    private final ActionListener equalsListener = e -> {
        if (calculator.isActiveOperandSet() && calculator.getPendingBinaryOperation() != null) {
            calculator.setValue(calculator.getPendingBinaryOperation().applyAsDouble(calculator.getActiveOperand(), calculator.getValue()));
            calculator.setPendingBinaryOperation(null);
        }
        calculator.notifyAllListeners();
    };

    /**
     * Factory method used to create all action listeners for unary operations.
     *
     * @param operator        non inverse operation
     * @param inverseOperator inverse operation if exists
     * @return action listener
     */
    private ActionListener listenerUnaryFactory(DoubleUnaryOperator operator, DoubleUnaryOperator inverseOperator) {
        return e -> {
            if (calculator.hasFrozenValue()) {
                throw new CalculatorInputException("Wrong button");
            }

            if (!inverse) {
                calculator.setValue(operator.applyAsDouble(calculator.getValue()));
            } else {
                if (inverseOperator != null) {
                    calculator.setValue(inverseOperator.applyAsDouble(calculator.getValue()));
                } else {
                    calculator.setValue(operator.applyAsDouble(calculator.getValue()));
                }
            }
        };
    }

    /**
     * Factory method used to create all action listeners for binary operations.
     *
     * @param operator        non inverse operation
     * @param inverseOperator inverse operation if exists
     * @return action listener
     */
    private ActionListener listenerBinaryFactory(DoubleBinaryOperator operator, DoubleBinaryOperator inverseOperator) {
        return e -> {
            if (calculator.hasFrozenValue()) {
                throw new CalculatorInputException("Wrong button");
            }

            DoubleBinaryOperator currentOperator = calculator.getPendingBinaryOperation();
            if (currentOperator != null) {
                calculator.setValue(currentOperator.applyAsDouble(calculator.getActiveOperand(), calculator.getValue()));
                calculator.setActiveOperand(calculator.getValue());
            } else {
                calculator.setActiveOperand(calculator.getValue());
                calculator.setValue(calculator.getValue());
            }

            calculator.freezeValue(String.valueOf(calculator.getActiveOperand()));
            calculator.clear();

            if (!inverse) {
                calculator.setPendingBinaryOperation(operator);
            } else {
                if (inverseOperator != null) {
                    calculator.setPendingBinaryOperation(inverseOperator);
                } else {
                    calculator.setPendingBinaryOperation(operator);
                }
            }

        };
    }

    /**
     * Method that will design and add given action listeners to them.
     *
     * @param button         to be designed
     * @param actionListener to be added to the button
     * @return designed button
     */
    private JButton buttonDesign(JButton button, ActionListener actionListener) {
        button.setBackground(Color.white);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        button.addActionListener(actionListener);

        if (button.getText().matches("[0-9]")) {
            button.setFont(button.getFont().deriveFont(30f));
        }

        return button;
    }

    /**
     * Method that will deign, add given action listeners to them and appoint inverted names for necessary functions.
     *
     * @param button         to be designed
     * @param actionListener to be added to the button
     * @param invertedName   inverted name of function
     * @return designed button
     */
    private JButton buttonDesign(JButton button, ActionListener actionListener, String invertedName) {
        JButton designedButton = buttonDesign(button, actionListener);
        invertedNames.put(button.getText(), invertedName);
        notInvertedNames.put(invertedName, button.getText());
        return designedButton;
    }

    /**
     * Method that will change the names of functions according to state of calculator.
     *
     * @param map of layout elements
     */
    private void invertNames(Map<RCPosition, Component> map) {
        for (Component component : map.values()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;

                if (inverse) {
                    if (invertedNames.containsKey(button.getText())) {
                        button.setText(invertedNames.get(button.getText()));
                    }
                } else {
                    if (notInvertedNames.containsKey(button.getText())) {
                        button.setText(notInvertedNames.get(button.getText()));
                    }
                }
            }
        }
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setSize(600, 600);
            calculator.setTitle("Calculator");
            calculator.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            calculator.setVisible(true);
        });
    }
}
