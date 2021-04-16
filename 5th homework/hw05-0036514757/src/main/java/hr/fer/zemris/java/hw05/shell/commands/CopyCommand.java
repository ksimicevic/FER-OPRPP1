package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CopyCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = Util.parseArguments(arguments);

        if (args.length != 2) {
            env.writeln("Expected two arguments");
            return ShellStatus.CONTINUE;
        }

        File source = new File(args[0]);

        if (!source.isFile()) {
            env.writeln("Expected file as source to copy from");
            return ShellStatus.CONTINUE;
        }

        File destination = new File(args[1]);

        if (destination.isFile() && destination.exists()) {
            env.writeln("Do you want to override the file? Write yes to proceed.");
            if (!env.readLine().toLowerCase().equals("yes")) {
                env.writeln("Copy command has been cancelled. The file won't be copied.");
            }
        }

        try (Reader reader = Files.newBufferedReader(Path.of(source.getPath()));
             Writer writer = Files.newBufferedWriter(destination.isDirectory() ? Path.of(destination + "\\" +  source.getName()): destination.toPath())) {
            char[] read = new char[4096];
            int charsRead;

            while ((charsRead = reader.read(read)) > -1) {
                writer.write(read, 0, charsRead);
            }


        } catch (IOException ex) {
            throw new ShellIOException("Unable to read/write");
        }

        env.writeln("Copy successful.");
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> list = new LinkedList<>();
        list.add("Copy command accepts two parameters representing source file and destination file name.");
        list.add("The command then will copy the contents of source file to provided destination");
        return Collections.unmodifiableList(list);
    }
}
