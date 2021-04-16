package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TreeCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String arg = Util.parseArguments(arguments)[0];
        File dir = new File(arg);

        if (!dir.isDirectory()) {
            env.writeln("Expected a directory for tree argument");
            return ShellStatus.CONTINUE;
        } else {
            printTree(env, dir, 0);
        }

        return ShellStatus.CONTINUE;
    }

    private void printTree(Environment env, File dir, int indent) {
        env.writeln(" ".repeat(indent) + dir.getName());

        var children = dir.listFiles();

        if (children != null) {
            for (File child : children) {
                if (child.isDirectory()) {
                    printTree(env, child, indent + 2);
                } else {
                    env.writeln(" ".repeat(indent + 2) + child.getName());
                }
            }
        }
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Tree command expects a single argument that represents a path to directory.");
        list.add("The command will print a tree.");
        return Collections.unmodifiableList(list);
    }
}
