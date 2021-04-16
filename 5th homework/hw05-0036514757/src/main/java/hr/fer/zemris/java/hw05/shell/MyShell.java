package hr.fer.zemris.java.hw05.shell;

import hr.fer.zemris.java.hw05.shell.commands.*;

import java.util.*;

public class MyShell implements Environment {
    private static final String HELLO_MESSAGE = "Welcome to MyShell v 1.0";

    private char PROMPT;
    private char MORELINES;
    private char MULTILINE;

    private SortedMap<String, ShellCommand> commands;
    private ShellStatus status;
    private Scanner sc;

    /**
     * Creates a new shell with default parameters.
     */
    public MyShell() {
        status = ShellStatus.CONTINUE;
        PROMPT = '>';
        MORELINES = '\\';
        MULTILINE = '|';
        commands = commands();
        sc = new Scanner(System.in);
    }

    /**
     * Starts the shell and waits for next command to execute.
     * The shell will wait and execute commands as long as command "exit" isn't executed.
     */
    public void execute() {
        writeln(HELLO_MESSAGE);

        while (true) {
            write(PROMPT + " ");
            String[] cmd = this.getNextCommand();

            if (commands.get(cmd[0]).executeCommand(this, cmd[1]) == ShellStatus.TERMINATE) {
                break;
            }
        }

        System.exit(0);
    }

    /**
     * Gets the next command that was written in command line and separates it in two parts:
     * First element of returning array will be command name, and second will be remaining arguments if there are such.
     *
     * @return string array of two elements consisting of command name and command arguments
     */
    public String[] getNextCommand() {
        String cmd = readLine();
        String[] args = cmd.split("\\s+");

        if (args.length == 1) {
            return new String[]{args[0].trim(), " "};
        } else if (args[1].equals(Character.toString(MORELINES))) {
            LinkedList<String> commandParts = new LinkedList<>();

            write(MULTILINE + " ");
            String cmdPart = readLine();

            while (true) {
                int indexOfMorelines = cmdPart.indexOf(MORELINES);

                if (indexOfMorelines > 0) {
                    commandParts.add(cmdPart.substring(0, indexOfMorelines).trim());
                    write(MULTILINE + " ");
                    cmdPart = readLine();
                } else {
                    commandParts.add(cmdPart);
                    break;
                }
            }

            return new String[]{args[0].trim(), String.join(" ", commandParts)};

        } else {
            String arguments = "";

            for (int i = 1; i < args.length; i++) {
                arguments = arguments + args[i] + " ";
            }
            return new String[]{args[0].trim(), arguments.trim()};
        }
    }


    @Override
    public String readLine() throws ShellIOException {
        return sc.nextLine();
    }

    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        SortedMap<String, ShellCommand> commands = new TreeMap<>();
        commands.put("symbol", new SymbolCommand());
        commands.put("exit", new ExitShellCommand());
        commands.put("charset", new CharsetsCommand());
        commands.put("cat", new CatCommand());
        commands.put("help", new HelpCommand());
        commands.put("tree", new TreeCommand());
        commands.put("copy", new CopyCommand());
        commands.put("mkdir", new MkdirCommand());
        commands.put("ls", new LsCommand());
        commands.put("hexdump", new HexdumpCommand());
        return Collections.unmodifiableSortedMap(commands);
    }

    @Override
    public Character getMultilineSymbol() {
        return MULTILINE;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        this.MULTILINE = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return PROMPT;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        this.PROMPT = symbol;
    }

    @Override
    public Character getMorelinesSymbol() {
        return MORELINES;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        this.MORELINES = symbol;
    }

    public static void main(String... args) {
        MyShell shell = new MyShell();
        shell.execute();
    }
}
