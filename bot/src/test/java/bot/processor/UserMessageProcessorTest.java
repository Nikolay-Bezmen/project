package bot.processor;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.links.UserIdLinks;
import edu.java.bot.processor.UserMessageProcessorImpl;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.commands.StartCommand.START_WORK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserMessageProcessorTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @InjectMocks
    private UserMessageProcessorImpl userMessageProcessorImpl;
    private void setUp(String commandText, Long chatId) {
        doReturn(message).when(update).message();
        doReturn(commandText).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(chatId).when(chat).id();
    }

    @Test
    @DisplayName("тестирование метода commands который должен вернуть все доступные комманды")
    void testCommands() {
        var correctCommandList = List.of(
            mock(StartCommand.class),
            mock(ListCommand.class),
            mock(UnTrackCommand.class),
            mock(TrackCommand.class),
            mock(HelpCommand.class)
        );

        var result = userMessageProcessorImpl.commands();

        assertThat(result).usingRecursiveComparison().isEqualTo(correctCommandList);
    }

    @Test
    void testStartCommandProcess() {
        Long chatId = 1L;
        String correctTextSendMessage = START_WORK;
        setUp("/start", chatId);

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    void testHelpCommandProcess() {
        Long chatId = 2L;
        String correctTextSendMessage = "/help - выводит окно с командами\n\n" +
            "/list - показывает список отслеживаемых ссылок\n\n" +
            "/start - регистрирует пользователя\n\n" +
            "/track - начинает отслеживание ссылки." +
            " Чтобы начать отслеживание ссылки введите комманду \"/track ваша_ссылка\"\n\n" +
            "/untrack - прекращает отслеживание ссылки." +
            " Чтобы закончить отслеживание ссылки введите комманду \"/untrack ваша_ссылка\"\n\n";
        setUp("/help", chatId);

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    void testListCommandProcess() {
        Long chatId = 3L;
        String correctTextSendMessage = "нет отслежтиваемых ссылок";
        setUp("/list", chatId);

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    void testTrackCommandProcess() {
        Long chatId = 4L;
        String linkUri = "https://github.com/Nikolay-Bezmen";
        String correctTextSendMessage = "ссылка \"%s\" с этого момента отслеживается".formatted(linkUri);
        setUp("/track %s".formatted(linkUri), chatId);

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());

        UserIdLinks.unTrackLink(URI.create(linkUri), chatId);
    }

    @Test
    void testUnTrackCommandProcess() {
        Long chatId = 5L;
        String linkUri = "https://github.com/Nikolay-Bezmen";
        String correctTextSendMessage = "ссылки \"%s\" нет в списке отслеживаемых".formatted(linkUri);
        setUp("/untrack %s".formatted(linkUri), chatId);

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

}
