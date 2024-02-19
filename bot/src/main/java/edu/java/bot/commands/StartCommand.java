package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    public static final String DESCRIPTION_TEXT = "регистрирует пользователя";
    public static final String COMMAND_TEXT = "/start";
    public static final String START_WORK = "начинаем работу...";

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
        return new SendMessage(update.message().chat().id(), START_WORK);
    }
}
