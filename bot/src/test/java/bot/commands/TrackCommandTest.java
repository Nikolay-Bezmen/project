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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.commands.TrackCommand.ALREADY_EXISTS;
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
    @Mock
    private UserIdLinks userIdLinks;

    @InjectMocks
    private TrackCommand trackCommand;

    @Test
    @DisplayName("тестирование метода description")
    void testDescription() {
        assertThat(trackCommand.description()).isEqualTo(trackCommand.DESCRIPTION_TEXT);
    }

    @Test
    @DisplayName("тестирование метода command")
    void testCommand() {
        assertThat(trackCommand.command()).isEqualTo(trackCommand.COMMAND_TEXT);
    }

    @Test
    @DisplayName("тестирование метода handle если комманда корректна")
    void testHandleWithValidURL() {
        String validURL = "https://example.com";
        String commandText = "/track " + validURL;
        String expectedResponse = "ссылка \"" + validURL + "\" с этого момента отслеживается";

        doReturn(message).when(update).message();
        doReturn(commandText).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(123456L).when(chat).id();
        doReturn(true).when(userIdLinks).addTrackLink(Mockito.any(Link.class), Mockito.any(Long.class));

        SendMessage sendMessage = trackCommand.handle(update);
        SendMessage correctSendMessage = new SendMessage(message.chat().id(), expectedResponse);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    @DisplayName("тестирование метода handle если ссылка уже отслеживается")
    void testHandleIfLinkAlreadyExistsInTrackList() {
        String validURL = "https://example.com";
        String commandText = "/track " + validURL;
        String expectedResponse = ALREADY_EXISTS.formatted(validURL);

        doReturn(message).when(update).message();
        doReturn(commandText).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(123456L).when(chat).id();
        doReturn(false).when(userIdLinks).addTrackLink(Mockito.any(Link.class), Mockito.any(Long.class));

        SendMessage sendMessage = trackCommand.handle(update);
        SendMessage correctSendMessage = new SendMessage(message.chat().id(), expectedResponse);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    @DisplayName("тестирование метода handle если ссылка не корректна")
    void testHandleIfLinkIsNotCorrect() {
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
    void testHandleIfCodeResponseIsNotOk() {
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
