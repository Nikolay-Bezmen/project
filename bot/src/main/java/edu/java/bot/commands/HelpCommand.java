package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {
    public static final String DESCRIPTION_TEXT = "выводит окно с командами";
    public static final String COMMAND_TEXT = "/help";
    private final List<Command> commands;

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
        List<Command> commandList = commands
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
