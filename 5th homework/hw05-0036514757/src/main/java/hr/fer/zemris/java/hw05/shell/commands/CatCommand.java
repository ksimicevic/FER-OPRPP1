package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CatCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = Util.parseArguments(arguments);

        try (FileInputStream is = new FileInputStream(args[0]);
             InputStreamReader isr = new InputStreamReader(is, args.length == 2 ? Charset.forName(args[1].trim()) : Charset.defaultCharset());
             BufferedReader buffReader = new BufferedReader(isr)) {
            String line = buffReader.readLine();

            while (line != null) {
                env.writeln(line);
                line = buffReader.readLine();
            }

        } catch (IOException ex) {
            env.writeln("Couldn't read/write to file");
            return ShellStatus.CONTINUE;
        }


        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Command cat takes two arguments.");
        list.add("First argument is path to some file and is mandatory");
        list.add("Second argument is charset name used to interpret chars from bytes.");
        list.add("Unless provided, a default charset will be used.");
        list.add("Afterwards, the shell will open the file and show it in console");
        return Collections.unmodifiableList(list);
    }
}
