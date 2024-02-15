package bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.links.Link;
import edu.java.bot.links.UserIdLinks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.time.LocalDateTime;
import static edu.java.bot.commands.TrackCommand.ADDED_IS_SUCCESS;
import static edu.java.bot.commands.TrackCommand.ALREADY_EXISTS;
import static edu.java.bot.commands.TrackCommand.COMMAND_TEXT;
import static edu.java.bot.commands.TrackCommand.DESCRIPTION_TEXT;
import static edu.java.bot.utils.UrlValidationUtils.INCORRECT_URL;
import static edu.java.bot.utils.UrlValidationUtils.PROBLEMS_WITH_ACCESS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TrackCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @InjectMocks
    private TrackCommand trackCommand;

    @Test
    @DisplayName("тестирование метода description")
    void testDescription(){
        String description = trackCommand.description();
        String correctDescription = DESCRIPTION_TEXT;

        assertThat(description).isEqualTo(correctDescription);
    }


    @Test
    @DisplayName("тестирование метода command")
    void testCommand(){
        String command = trackCommand.command();
        String correctCommand = COMMAND_TEXT;

        assertThat(command).isEqualTo(correctCommand);
    }

    @Test
    @DisplayName("тестирование метода handle если комманда польностью корректна")
    void testHandleIfTheCommandIsCompletelyCorrect(){
        String linkUri = "https://github.com/Nikolay-Bezmen";
        String messageText = "/track %s".formatted(linkUri);
        Long userId = 1L;

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();
        doReturn(messageText).when(message).text();


        SendMessage correctSendMessage = new SendMessage(userId, ADDED_IS_SUCCESS.formatted(linkUri));
        SendMessage sendMessage = trackCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    @DisplayName("тестирование метода handle если ссылка уже отслеживается")
    void testHandleIfLinkAlreadyExistsInTrackList(){
        String linkUri = "https://github.com/Nikolay-Bezmen";
        String messageText = "/track %s".formatted(linkUri);
        Long userId = 1L;

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();
        doReturn(messageText).when(message).text();

        Link link = new Link(URI.create(linkUri), LocalDateTime.MIN);
        UserIdLinks.addTrackLink(link, userId);

        SendMessage correctSendMessage = new SendMessage(userId, ALREADY_EXISTS.formatted(linkUri));
        SendMessage sendMessage = trackCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());

        UserIdLinks.unTrackLink(URI.create(linkUri), userId);
    }

    @Test
    @DisplayName("тестирование метода handle если ссылка не корректна")
    void testHandleIfLinkIsNotCorrect(){
        String linkUri = "мусор";
        String messageText = "/track %s".formatted(linkUri);
        Long userId = 1L;

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();
        doReturn(messageText).when(message).text();

        SendMessage correctSendMessage = new SendMessage(userId, INCORRECT_URL.formatted(linkUri));
        SendMessage sendMessage = trackCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    @DisplayName("тестирование метода handle если нет доступа на сайт по ссылке")
    void testHandleIfCodeResponseIsNotOk(){
        String linkUri = "https://github.com/Nikolay-Bezmeboqugf823f98fh91cmo2";
        String messageText = "/track %s".formatted(linkUri);
        Long userId = 1L;

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();
        doReturn(messageText).when(message).text();

        SendMessage correctSendMessage = new SendMessage(userId, PROBLEMS_WITH_ACCESS.formatted(linkUri));
        SendMessage sendMessage = trackCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }
}
