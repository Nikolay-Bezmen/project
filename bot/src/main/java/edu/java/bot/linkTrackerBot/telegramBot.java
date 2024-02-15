package edu.java.bot.linkTrackerBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.processor.UserMessageProcessor;
import edu.java.bot.processor.UserMessageProcessorImpl;
import edu.java.bot.utils.CommandUtils;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("TypeName")
@Component
public class telegramBot implements Bot {
    private final static Logger LOGGER = LogManager.getLogger();
    @Autowired private ApplicationConfig applicationConfig;
    private static TelegramBot bot;

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        UserMessageProcessor userMessageProcessor = new UserMessageProcessorImpl();

        for (Update update : updates) {
            if (update.message() != null && update.message().text() != null && !update.message().text().isEmpty()) {
                SendMessage sendMessage = userMessageProcessor.process(update);
                bot.execute(sendMessage);
            }
        }
        return 1;
    }

    @PostConstruct
    @Override
    public void start() {
        bot = new TelegramBot(applicationConfig.telegramToken());

        SetMyCommands setMyCommands = createMenu();
        BaseResponse response = bot.execute(setMyCommands);
        if (!response.isOk()) {
            LOGGER.error("Error updating bot commands: " + response.description());
        }

        bot.setUpdatesListener(updates -> {
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

    private static SetMyCommands createMenu() {
        SetMyCommands setMyCommands = new SetMyCommands(CommandUtils.getCommands().stream()
            .map(command -> {
                BotCommand bc = new BotCommand(command.command(), command.description());
                return bc;
            }).toArray(BotCommand[]::new));

        return setMyCommands;
    }

    @Override
    public void close() {

    }
}
