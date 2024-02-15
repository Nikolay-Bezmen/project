package bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.UnTrackCommand;
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
import static edu.java.bot.commands.UnTrackCommand.COMMAND_TEXT;
import static edu.java.bot.commands.UnTrackCommand.CURRENT_LINK_UNTRACK;
import static edu.java.bot.commands.UnTrackCommand.DESCRIPTION_TEXT;
import static edu.java.bot.commands.UnTrackCommand.LINK_UNTRACKED_IS_CORRECT;
import static edu.java.bot.utils.UrlValidationUtils.INCORRECT_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UnTrackCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @InjectMocks
    private UnTrackCommand unTrackCommand;

    @Test
    @DisplayName("тестирование метода description")
    void testDescription(){
        String description = unTrackCommand.description();
        String correctDescription = DESCRIPTION_TEXT;

        assertThat(description).isEqualTo(correctDescription);
    }


    @Test
    @DisplayName("тестирование метода command")
    void testCommand(){
        String command = unTrackCommand.command();
        String correctCommand = COMMAND_TEXT;

        assertThat(command).isEqualTo(correctCommand);
    }

    @Test
    @DisplayName("тестирование метода handle если комманда польностью корректна")
    void testHandleIfCommandIsCompletelyCorrect(){
        String linkUri = "https://github.com/Nikolay-Bezmen";
        String messageText = "/untrack %s".formatted(linkUri);
        Long userId = 1L;

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();
        doReturn(messageText).when(message).text();

        Link link = new Link(URI.create(linkUri), LocalDateTime.MIN);
        UserIdLinks.addTrackLink(link, userId);

        SendMessage correctSendMessage = new SendMessage(userId, LINK_UNTRACKED_IS_CORRECT.formatted(linkUri));
        SendMessage sendMessage = unTrackCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }


    @Test
    @DisplayName("тестирование метода handle если ссылка не отслеживается")
    void testHandleIfLinkUnTrack(){
        String linkUri = "https://github.com/Nikolay-Bezmen";
        String messageText = "/untrack %s".formatted(linkUri);
        Long userId = 1L;

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();
        doReturn(messageText).when(message).text();

        SendMessage correctSendMessage = new SendMessage(userId, CURRENT_LINK_UNTRACK.formatted(linkUri));
        SendMessage sendMessage = unTrackCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    @DisplayName("тестирование метода handle если комманда польностью некорректна")
    void testHandleIfCommandIsNotCorrect(){
        String linkUri = "мусор";
        String messageText = "/untrack %s".formatted(linkUri);
        Long userId = 1L;

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();
        doReturn(messageText).when(message).text();

        SendMessage correctSendMessage = new SendMessage(userId, INCORRECT_URL.formatted(linkUri));
        SendMessage sendMessage = unTrackCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }
}
