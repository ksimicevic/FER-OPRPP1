package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ExitShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Shuts down the shell command.");
        return Collections.unmodifiableList(list);
    }
}
