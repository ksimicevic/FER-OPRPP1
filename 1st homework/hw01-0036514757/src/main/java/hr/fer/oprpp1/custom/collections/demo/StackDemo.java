package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {

    public static void main(String... args) {
        ObjectStack stack = new ObjectStack();
        String[] parts = args[0].split(" ");

        for (String part : parts) {
            try {
                Integer i = Integer.parseInt(part);
                stack.push(i);
            } catch (NumberFormatException ex) {
                Integer i1 = (Integer) stack.pop();
                Integer i2 = (Integer) stack.pop();

                Integer res = switch (part) {
                    case "+":
                        yield i1 + i2;
                    case "-":
                        yield i2 - i1;
                    case "/":
                        yield i2 / i1;
                    case "*":
                        yield i1 * i2;
                    case "%":
                        yield i2 % i1;
                    default:
                        yield 0;
                };

                stack.push(res);
            }
        }

        if (stack.size() != 1) {
            System.out.println("Error: something went wrong.");
        } else {
            System.out.println("Expression evaluates to " + stack.pop());
        }

    }
}
