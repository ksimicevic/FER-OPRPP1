package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CharsetsCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        var charsets = Charset.availableCharsets();
        for (var charset : charsets.keySet()) {
            env.writeln(charset);
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Returns list of supported charsets for this Java platform.");
        return Collections.unmodifiableList(list);
    }
}
