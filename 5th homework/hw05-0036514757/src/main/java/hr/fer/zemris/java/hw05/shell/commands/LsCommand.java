package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class LsCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String directory = Util.parseArguments(arguments)[0];


        File path = new File(directory);

        var children = path.listFiles();

        if(children != null) {
            for(var child: children) {
                printList(env, Path.of(String.valueOf(child)));
            }
        } else {
            env.write("Given path is empty or isn't a directory");
        }

        return ShellStatus.CONTINUE;
    }

    private void printList(Environment env, Path path) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        BasicFileAttributeView faView = Files.getFileAttributeView(
                path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );

        BasicFileAttributes attributes;
        try {
            attributes = faView.readAttributes();
        } catch (IOException ex) {
            throw new ShellIOException("Error occurred while reading directory attributes");
        }

        FileTime fileTime = attributes.creationTime();

        if (attributes.isDirectory()) {
            env.write("d");
        } else {
            env.write("-");
        }

        if (Files.isReadable(path)) {
            env.write("r");
        } else {
            env.write("-");
        }

        if (Files.isWritable(path)) {
            env.write("w");
        } else {
            env.write("-");
        }

        if (Files.isExecutable(path)) {
            env.write("x");
        } else {
            env.write("-");
        }

        env.write(" ");

        String size = String.valueOf(attributes.size());
        env.write(" ".repeat(10 - size.length()) + size);

        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

        env.write(" " + formattedDateTime);

        env.write(" " + path.getFileName());
        env.write("\n");
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Command ls takes a single argument that represents a directory");
        list.add("and it writes a directory listing.");
        return Collections.unmodifiableList(list);
    }
}
