package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.Link;
import edu.java.bot.links.UserIdLinks;
import edu.java.bot.utils.UrlValidationUtils;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.UrlValidationUtils.LINK_IS_VALID;


@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final UserIdLinks userIdLinks;
    public static final String DESCRIPTION_TEXT = "начинает отслеживание ссылки."
        + " Чтобы начать отслеживание ссылки введите комманду \"/track ваша_ссылка\"";
    public static final String COMMAND_TEXT = "/track";
    public static final String ALREADY_EXISTS = "ссылка \"%s\" уже отслеживается";
    public static final String ADDED_IS_SUCCESS = "ссылка \"%s\" с этого момента отслеживается";

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
        String url = userIdLinks.getLinkIfPossible(update.message().text());

        Long userId = update.message().chat().id();

        String response = "akengopiqngoqi";
        String checkValidateMessage = UrlValidationUtils.validateLink(url);
        //проверка на то что ссылка корректная;
        if (checkValidateMessage.equals(LINK_IS_VALID)) {

            int timestamp = update.message().date();
            Instant instant = Instant.ofEpochSecond(timestamp);
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            Link link = new Link(URI.create(url), ldt);
            //проверка что ссылка ещё не отслеживается
            if (userIdLinks.addTrackLink(link, userId)) {
                response = ADDED_IS_SUCCESS.formatted(url);
            } else {
                response = ALREADY_EXISTS.formatted(url);
            }
        } else {
            response = checkValidateMessage.formatted(url);
        }

        return new SendMessage(update.message().chat().id(), response);
    }
}
