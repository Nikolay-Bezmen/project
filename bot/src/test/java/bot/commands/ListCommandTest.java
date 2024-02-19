package bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.links.Link;
import edu.java.bot.links.UserIdLinks;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.commands.ListCommand.COMMAND_TEXT;
import static edu.java.bot.commands.ListCommand.DESCRIPTION_TEXT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Mock
    private UserIdLinks userIdLinks; // Создаем заглушку для UserIdLinks

    @InjectMocks
    private ListCommand listCommand;

    @Test
    @DisplayName("тестирование метода description")
    void testDescription() {
        assertThat(listCommand.description()).isEqualTo(listCommand.DESCRIPTION_TEXT);
    }

    @Test
    @DisplayName("тестирование метода command")
    void testCommand() {
        assertThat(listCommand.command()).isEqualTo(listCommand.COMMAND_TEXT);
    }

    @Test
    @DisplayName("тестирование метода handle")
    void testHandle() {
        Long userId = 1L;
        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();

        String messageText = "1. https://github.com/Nikolay-Bezmen," +
            " отслеживается с  -999999999-01-01T00:00\n";
        SendMessage correctSendMessage = new SendMessage(userId, messageText);
        Link gitHub = new Link(URI.create("https://github.com/Nikolay-Bezmen"), LocalDateTime.MIN);

        doReturn(Set.of(gitHub)).when(userIdLinks).getTrackList(userId);
        SendMessage sendMessage = listCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }
}
