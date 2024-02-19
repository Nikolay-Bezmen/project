package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@SuppressWarnings("ImportOrder")
@Component
@RequiredArgsConstructor
public class UserMessageProcessorImpl implements UserMessageProcessor {
    public static final String SUCH_COMMAND_IS_NOT_EXIST = "такая команда не поддерживатеся =(";
    private final List<Command> commands;

    @Override public List<? extends Command> commands() {
        return commands;
    }

    @Override public SendMessage process(Update update) {
        String messageText = update.message().text();

        Optional<Command> optionalCommand = getCommand(messageText);

        return optionalCommand.isPresent()
            ? optionalCommand.get().handle(update)
            : new SendMessage(update.message().chat().id(), SUCH_COMMAND_IS_NOT_EXIST);
    }

    public Optional<Command> getCommand(String command) {
        for (Command cmd : commands) {
            if (command.startsWith(cmd.command())) {
                return Optional.of(cmd);
            }
        }

        return Optional.empty();
    }
}
