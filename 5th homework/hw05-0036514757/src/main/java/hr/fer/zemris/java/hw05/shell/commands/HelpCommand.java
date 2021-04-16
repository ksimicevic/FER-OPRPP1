package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HelpCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        if (arguments.equals(" ")) {
            for (var cmd : env.commands().entrySet()) {
                env.writeln(cmd.getKey());
            }
        } else {
            String cmdName = arguments.trim();
            ShellCommand command = env.commands().get(cmdName);

            if (command == null) {
                env.writeln("No such command.");
            } else {
                for (String line : command.getCommandDescription()) {
                    env.writeln(line);
                }
            }
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> list = new LinkedList<>();
        list.add("Use help command to get a list of all available commands.");
        list.add("Use help along with command name to find out more about the command.");
        return Collections.unmodifiableList(list);
    }
}
