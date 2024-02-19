package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.Link;
import edu.java.bot.links.UserIdLinks;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final UserIdLinks userIdLinks;
    public static final String DESCRIPTION_TEXT = "показывает список отслеживаемых ссылок";
    public static final String COMMAND_TEXT = "/list";
    private static final String FORMAT_LINK = "%d. %s\n";
    public static final String TRACK_LIST_IS_EMPTY = "нет отслежтиваемых ссылок";

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
        List<Link> trackList = userIdLinks.getTrackList(update.message().chat().id()).stream()
            .sorted(Comparator.comparing(Link::startTrackingDateTime))
            .toList();

        StringBuilder response = new StringBuilder();
        for (int i = 1; i <= trackList.size(); ++i) {
            response.append(FORMAT_LINK.formatted(i, trackList.get(i - 1).getInfo()));
        }

        if (response.isEmpty()) {
            response.append(TRACK_LIST_IS_EMPTY);
        }

        return new SendMessage(update.message().chat().id(), response.toString());
    }
}
