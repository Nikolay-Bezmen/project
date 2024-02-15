package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class UnKnownCommand implements Command {
    public static final String DESCRIPTION_TEXT = "такая комманда не поддерживается";

    @Override
    public String command() {
        return null;
    }

    @Override
    public String description() {
        return DESCRIPTION_TEXT;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), description());
    }
}
