package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.UserIdLinks;
import edu.java.bot.utils.UrlValidationUtils;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.UrlValidationUtils.LINK_IS_VALID;

@Component
@RequiredArgsConstructor
public class UnTrackCommand implements Command {
    private final UserIdLinks userIdLinks;
    public static final String DESCRIPTION_TEXT = "прекращает отслеживание ссылки. Чтобы закончить"
        + " отслеживание ссылки введите комманду \"/untrack ваша_ссылка\"";
    public static final String COMMAND_TEXT = "/untrack";
    public static final String LINK_UNTRACKED_IS_CORRECT = "ссылка \"%s\" больше не отслеживается";
    public static final String CURRENT_LINK_UNTRACK = "ссылки \"%s\" нет в списке отслеживаемых";

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
        String response;
        String checkValidateMessage = UrlValidationUtils.validateLink(url);
        Long userId = update.message().chat().id();

        if (checkValidateMessage.equals(LINK_IS_VALID)) {

            if (userIdLinks.unTrackLink(URI.create(url), userId)) {
                response = LINK_UNTRACKED_IS_CORRECT.formatted(url);
            } else {
                response = CURRENT_LINK_UNTRACK.formatted(url);
            }
        } else {
            response = checkValidateMessage.formatted(url);
        }

        return new SendMessage(update.message().chat().id(), response);
    }
}
