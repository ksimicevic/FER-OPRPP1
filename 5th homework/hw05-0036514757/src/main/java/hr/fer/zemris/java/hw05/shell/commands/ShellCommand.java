package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.FileNotFoundException;
import java.util.List;

public interface ShellCommand {
    ShellStatus executeCommand(Environment env, String arguments);
    String getCommandName();
    List<String> getCommandDescription();
}
