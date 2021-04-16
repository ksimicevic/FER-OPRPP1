package hr.fer.zemris.java.hw05.shell.commands;

import java.util.LinkedList;

public class Util {

    public static String[] parseArguments(String arguments) {
        int indexOfString = arguments.trim().indexOf('"');
        LinkedList<String> splitArgs = new LinkedList<>();

        if (indexOfString < 0) {
            if (arguments.contains(" ")) {
                return arguments.trim().split("\\s+");
            } else {
                return new String[]{arguments};
            }
        } else {
            char[] array = arguments.toCharArray();
            for (int i = 0; i < array.length; ) {

                int begin, end;

                if (array[i] == '"') {
                    begin = i;
                    end = begin;

                    for (int j = begin + 1; j < array.length; j++) {
                        if (array[j] == '"') {
                            end = j;
                            break;
                        }
                    }

                    splitArgs.add(arguments.substring(begin + 1, Math.min(end, arguments.length() - 1)));
                    i = end + 1;
                } else {
                    i++;
                }
            }

            return splitArgs.toArray(new String[]{"String"});
        }
    }
}
