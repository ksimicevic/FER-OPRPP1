package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SymbolCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = arguments.split("\\s+");
        int length = args.length;
        String str = args[0].toUpperCase();
        char c = ' ';

        if (length == 2) {
            c = args[1].toCharArray()[0];
        }

        switch (str) {
            case "PROMPT" -> {
                if (length == 1) {
                    env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'");
                } else {
                    env.writeln("Symbol for PROMPT changed from '" + env.getPromptSymbol() + "' to '" + c + "'");
                    env.setPromptSymbol(c);
                }
            }
            case "MORELINES" -> {
                if (length == 1) {
                    env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'");
                } else {
                    env.writeln("Symbol for MORELINES changed from '" + env.getMorelinesSymbol() + "' to '" + c + "'");
                    env.setMorelinesSymbol(c);
                }
            }
            case "MULTILINE" -> {
                if (length == 1) {
                    env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'");
                } else {
                    env.writeln("Symbol for MULTILINE changed from '" + env.getMultilineSymbol() + "' to '" + c + "'");
                    env.setMultilineSymbol(c);
                }
            }
            default -> {
                env.writeln("Invalid argument for symbol command");
                return ShellStatus.CONTINUE;
            }
        }


        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Symbol command is used to check and change symbol properties of shell.");
        list.add("Currently supported symbol properties are PROMPT, MULTILINE and MORELINES");
        list.add("To see what symbol is assigned to PROMPT, use 'symbol PROMPT'");
        list.add("To change symbol used for PROMPT, use 'symbol PROMPT #'");
        return Collections.unmodifiableList(list);
    }
}
