package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.utils.CommandUtils;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    public static final String DESCRIPTION_TEXT = "выводит окно с командами";
    public static final String COMMAND_TEXT = "/help";

    @Override
    public String command() {
        return COMMAND_TEXT;
    }

    @Override
    public String description() {
        return DESCRIPTION_TEXT;
    }

    @Override
    public SendMessage handle(Update update) {
        String pattern = "%s - %s\n\n";
        StringBuilder textOfAnswer = new StringBuilder();
        List<Command> commandList = CommandUtils
            .getCommands()
            .stream()
            .sorted(Comparator.comparing(Command::command))
            .toList();

        for (Command command : commandList) {
            textOfAnswer.append(pattern.formatted(command.command(), command.description()));
        }

        SendMessage sendMessage = new SendMessage(update.message().chat().id(), textOfAnswer.toString());
        return sendMessage;
    }

}
