package hr.fer.zemris.lystems.impl;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lystems.impl.commands.*;

import java.awt.*;

public class LSystemBuilderImpl implements LSystemBuilder {
    private Dictionary<String, Command> commands = new Dictionary<>();
    private Dictionary<String, String> productions = new Dictionary<>();
    private double unitLength;
    private double unitLengthDegreeScaler;
    private Vector2D origin;
    private double angle;
    private String axiom;

    public LSystemBuilderImpl() {
        this.unitLength = 0.1;
        this.unitLengthDegreeScaler = 1.0;
        this.origin = new Vector2D(0, 0);
        this.angle = 0.0;
        this.axiom = "";
    }

    public LSystemBuilderImpl(double unitLength, double unitLengthDegreeScaler, Vector2D origin, double angle, String axiom) {
        this.unitLength = unitLength;
        this.unitLengthDegreeScaler = unitLengthDegreeScaler;
        this.origin = origin;
        this.angle = angle;
        this.axiom = axiom;
    }

    @Override
    public LSystemBuilder setUnitLength(double v) {
        this.unitLength = v;
        return this;
    }

    @Override
    public LSystemBuilder setOrigin(double v, double v1) {
        this.origin = new Vector2D(v, v1);
        return this;
    }

    @Override
    public LSystemBuilder setAngle(double v) {
        this.angle = v;
        return this;
    }

    @Override
    public LSystemBuilder setAxiom(String s) {
        this.axiom = s;
        return this;
    }

    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double v) {
        this.unitLengthDegreeScaler = v;
        return this;
    }

    @Override
    public LSystemBuilder registerProduction(char c, String s) {
        productions.put(Character.toString(c), s);
        return this;
    }

    @Override
    public LSystemBuilder registerCommand(char c, String s) {
        String[] commandParts = s.split(" ");
        String command = commandParts[0];

        commands.put(Character.toString(c),
                switch (command.toLowerCase()) {
                    case "draw" -> new DrawCommand(Double.parseDouble(commandParts[1]));
                    case "skip" -> new SkipCommand(Double.parseDouble(commandParts[1]));
                    case "pop" -> new PopCommand();
                    case "push" -> new PushCommand();
                    case "rotate" -> new RotateCommand(Double.parseDouble(commandParts[1]));
                    case "color" -> new ColorCommand(Color.decode("0x" + commandParts[1]));
                    case "scale" -> new ScaleCommand(Double.parseDouble(commandParts[1]));
                    default -> throw new IllegalArgumentException("Couldn't parse command");
                }
        );

        return this;
    }

    @Override
    public LSystemBuilder configureFromText(String[] strings) {
        for (String string : strings) {
            if (string.isEmpty()) {
                continue;
            }

            String[] parts = string.split("\\s+");

            String begin = parts[0];

            switch (begin) {
                case "origin" -> this.setOrigin(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                case "angle" -> this.setAngle(Double.parseDouble(parts[1]));
                case "unitLength" -> this.setUnitLength(Double.parseDouble(parts[1]));
                case "unitLengthDegreeScaler" -> {
                    if (parts.length == 2) {
                        this.setUnitLengthDegreeScaler(Double.parseDouble(parts[1]));
                    } else {
                        char[] chars = string.toCharArray();
                        int index = 0;
                        index = skipWhiteSpacesOrLetter(chars, index);

                        StringBuilder sb = new StringBuilder();

                        for (int i = index; i < chars.length; i++) {
                            if (chars[i] == '/') {
                                index = ++i;
                                break;
                            } else {
                                sb.append(chars[i]);
                            }
                        }

                        double firstOperand = Double.parseDouble(sb.toString().trim());
                        sb.setLength(0);

                        index = skipWhiteSpacesOrLetter(chars, index);

                        for (int j = index; j < chars.length; j++) {
                            sb.append(chars[j]);
                        }

                        double secondOperand = Double.parseDouble(sb.toString().trim());
                        this.setUnitLengthDegreeScaler(firstOperand / secondOperand);

                    }
                }
                case "command" -> {
                    String symbol = parts[1];
                    if (symbol.length() == 1) {
                        this.registerCommand(symbol.toCharArray()[0], string.substring(string.indexOf(symbol) + 1).trim());
                    } else {
                        throw new IllegalArgumentException("Expected symbol but didn't find it");
                    }
                }
                case "axiom" -> this.setAxiom(parts[1]);
                case "production" -> {
                    String symbol = parts[1];
                    if (symbol.length() == 1) {
                        this.registerProduction(symbol.toCharArray()[0], string.substring(string.indexOf(symbol) + 1).trim());
                    } else {
                        throw new IllegalArgumentException("Expected symbol but didn't find it");
                    }
                }
                default -> throw new IllegalStateException("Couldn't parse");
            }

        }

        return this;
    }

    @Override
    public LSystem build() {
        return new LSystemImpl();
    }

    /**
     * Skips white spaces and letters. Used to parse UnitLengthDegreeScaler.
     *
     * @param array character array that is being parsed
     * @param index current index of parsing
     * @return index at first non-white or letter character
     */
    private int skipWhiteSpacesOrLetter(char[] array, int index) {
        while (index < array.length) {
            if (Character.isWhitespace(array[index]) || Character.isLetter(array[index])) {
                index++;
            } else {
                break;
            }
        }
        return index;
    }

    /**
     * Inner class implementing LSystem.
     */
    public class LSystemImpl implements LSystem {

        /**
         * Generates a string for given depth.
         *
         * @param i depth
         * @return generated string
         */
        @Override
        public String generate(int i) {
            if (i == 0) {
                return LSystemBuilderImpl.this.axiom;
            }

            String start = generate(i - 1);
            StringBuilder sb = new StringBuilder();
            for (char c : start.toCharArray()) {
                String production = LSystemBuilderImpl.this.productions.get(Character.toString(c));
                if (production != null) {
                    sb.append(production);
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        /**
         * Draws the picture for given depth.
         *
         * @param i       depth
         * @param painter painter used for drawing
         */
        @Override
        public void draw(int i, Painter painter) {
            Context context = new Context();
            double angle = LSystemBuilderImpl.this.angle;
            TurtleState turtleState =
                    new TurtleState(LSystemBuilderImpl.this.origin, new Vector2D(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle))), Color.BLACK, LSystemBuilderImpl.this.unitLength * Math.pow(LSystemBuilderImpl.this.unitLengthDegreeScaler, i));
            context.pushState(turtleState);


            char[] generated = generate(i).toCharArray();

            for (char c : generated) {
                Command command = LSystemBuilderImpl.this.commands.get(Character.toString(c));

                if (command != null) {
                    command.execute(context, painter);
                }
            }


        }
    }
}
