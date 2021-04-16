package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HexdumpCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String fileName = hr.fer.zemris.java.hw05.shell.commands.Util.parseArguments(arguments)[0];

        File file = new File(fileName);

        if (file.isDirectory()) {
            env.writeln("Given argument does not represent a file");
            return ShellStatus.CONTINUE;
        }

        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            byte[] array = new byte[16];
            int rowCounter = 0, bytesRead = 0;

            while ((bytesRead = inputStream.read(array)) > -1) {
                env.write(String.format("%08x", rowCounter) + ": ");
                String hexString = hr.fer.oprpp1.hw05.crypto.Util.bytetohex(array);

                env.write(formatHexString(hexString));
                env.write("| ");

                for (byte b : array) {
                    if (b >= 32) {
                        env.write(Character.toString(b));
                    } else {
                        env.write(".");
                    }
                }
                env.write("\n");
                rowCounter += 16;
            }
        } catch (IOException ex) {
            env.writeln("Couldn't read from given file.");
        }

        return ShellStatus.CONTINUE;
    }

    private String formatHexString(String hexString) {
        StringBuilder sb = new StringBuilder();
        char[] chars = hexString.toCharArray();

        for (int i = 0; i < chars.length; i += 2) {
            sb.append(chars[i]).append(chars[i + 1]).append(i == 14 ? " | " : " ");
        }

        return sb.toString();
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Hexdump command expects a single argument representing a file name");
        list.add("and the command will produce a hex-output of the file.");
        return Collections.unmodifiableList(list);
    }
}
