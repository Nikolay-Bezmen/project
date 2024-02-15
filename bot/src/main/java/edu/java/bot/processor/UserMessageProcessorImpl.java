package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.utils.CommandUtils;
import java.util.List;
import org.springframework.stereotype.Component;


@Component
@SuppressWarnings("ImportOrder")
public class UserMessageProcessorImpl implements UserMessageProcessor {
    @Override public List<? extends Command> commands() {
        return List.of(
            new StartCommand(),
            new ListCommand(),
            new UnTrackCommand(),
            new TrackCommand(),
            new HelpCommand()
        );
    }

    @Override public SendMessage process(Update update) {
        //получаем текст сообщения
        String messageText = update.message().text();

        //подбираем соответствующую комманду или говорим что такой нет
        Command command = CommandUtils.getCommand(messageText);

        return command.handle(update);
    }

}
