package edu.java.bot.utils;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnKnownCommand;
import edu.java.bot.commands.UnTrackCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandUtils {
    public static final String EMPTY_MESSAGE = "";

    private CommandUtils() {
    }

    private static final Command START = new StartCommand();
    private static final Command TRACK = new TrackCommand();
    private static final Command UNTRACK = new UnTrackCommand();
    private static final Command LIST = new ListCommand();
    private static final Command HELP = new HelpCommand();
    private static final Command UNKNOWN = new UnKnownCommand();
    private static final Map<String, Command> COMMANDS = Map.of(
        START.command(), START,
        HELP.command(), HELP,
        LIST.command(), LIST,
        TRACK.command(), TRACK,
        UNTRACK.command(), UNTRACK
    );

    public static Command getCommand(String command) {
        for (String key : COMMANDS.keySet()) {
            if (command.startsWith(key)) {
                return COMMANDS.get(key);
            }
        }
//        System.out.println("in getMethod");
        return UNKNOWN;
    }

    public static List<Command> getCommands() {
        return new ArrayList<>(COMMANDS.values());
    }
}
