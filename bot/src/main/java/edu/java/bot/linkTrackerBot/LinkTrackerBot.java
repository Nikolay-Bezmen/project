package edu.java.bot.linkTrackerBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.processor.UserMessageProcessor;
import edu.java.bot.processor.UserMessageProcessorImpl;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@SuppressWarnings("TypeName")
@Component
public class LinkTrackerBot extends TelegramBot {
    private final static Logger LOGGER = LogManager.getLogger();
    private final List<Command> commands;

    public LinkTrackerBot(ApplicationConfig config, List<Command> commands) {
        super(config.telegramToken());
        this.commands = commands;
        SetMyCommands setMyCommands = createMenu();
        execute(setMyCommands);

        setUpdatesListener(updates -> {
            process(updates);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                LOGGER.error(e.getStackTrace());
            }
        });
    }

    //    @Override
    public int process(List<Update> updates) {
        UserMessageProcessor userMessageProcessor = new UserMessageProcessorImpl(commands);

        for (Update update : updates) {
            if (update.message() != null && update.message().text() != null && !update.message().text().isEmpty()) {
                SendMessage sendMessage = userMessageProcessor.process(update);
                execute(sendMessage);
            }
        }

        return 1;
    }

    private SetMyCommands createMenu() {
        SetMyCommands setMyCommands = new SetMyCommands(commands.stream()
            .map(command -> {
                BotCommand bc = new BotCommand(command.command(), command.description());
                return bc;
            }).toArray(BotCommand[]::new));

        return setMyCommands;
    }
}
