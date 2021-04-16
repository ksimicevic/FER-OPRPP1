package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MkdirCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String directory = Util.parseArguments(arguments)[0];

        try {
            Files.createDirectories(Path.of(directory));
            env.writeln("Successfully created new directory/directories");
        } catch (IOException ex) {
            env.writeln("Unable to create directory.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Mkdir command takes one argument representing directory name");
        list.add("It will create appropriate directory structure");
        return Collections.unmodifiableList(list);
    }
}
