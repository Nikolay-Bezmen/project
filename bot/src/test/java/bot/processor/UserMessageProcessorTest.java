package bot.processor;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendAnimation;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.links.UserIdLinks;
import edu.java.bot.processor.UserMessageProcessorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.util.List;
import static edu.java.bot.commands.ListCommand.TRACK_LIST_IS_EMPTY;
import static edu.java.bot.commands.StartCommand.START_WORK;
import static edu.java.bot.commands.TrackCommand.ADDED_IS_SUCCESS;
import static edu.java.bot.commands.UnTrackCommand.CURRENT_LINK_UNTRACK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

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
    private static final Long chatId = 1L;
    private void setUp(String commandText){
        doReturn(message).when(update).message();
        doReturn(commandText).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(chatId).when(chat).id();
    }
    @Test
    @DisplayName("тестирование метода commands который должен вернуть все доступные комманды")
    void testCommands(){
        var correctCommandList = List.of(
            new StartCommand(),
            new ListCommand(),
            new UnTrackCommand(),
            new TrackCommand(),
            new HelpCommand());

        var result = userMessageProcessorImpl.commands();

        assertThat(result).usingRecursiveComparison().isEqualTo(correctCommandList);
    }

    @Test
    void testStartCommandProcess(){
        String correctTextSendMessage = START_WORK;
        setUp("/start");

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    void testHelpCommandProcess(){
        String correctTextSendMessage = "/help - выводит окно с командами\n\n" +
            "/list - показывает список отслеживаемых ссылок\n\n" +
            "/start - регистрирует пользователя\n\n" +
            "/track - начинает отслеживание ссылки." +
            " Чтобы начать отслеживание ссылки введите комманду \"/track ваша_ссылка\"\n\n" +
            "/untrack - прекращает отслеживание ссылки." +
            " Чтобы закончить отслеживание ссылки введите комманду \"/untrack ваша_ссылка\"\n\n";
        setUp("/help");

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    void testListCommandProcess(){
        String correctTextSendMessage = TRACK_LIST_IS_EMPTY;
        setUp("/list");

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    void testTrackCommandProcess(){
        String linkUri = "https://github.com/Nikolay-Bezmen";
        String correctTextSendMessage = ADDED_IS_SUCCESS.formatted(linkUri);
        setUp("/track %s".formatted(linkUri));

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());

        UserIdLinks.unTrackLink(URI.create(linkUri), chatId);
    }

    @Test
    void testUnTrackCommandProcess(){
        String linkUri = "https://github.com/Nikolay-Bezmen";
        String correctTextSendMessage = CURRENT_LINK_UNTRACK.formatted(linkUri);
        setUp("/untrack %s".formatted(linkUri));

        SendMessage correctSendMessage = new SendMessage(chatId, correctTextSendMessage);
        SendMessage result = userMessageProcessorImpl.process(update);

        assertThat(result.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }




}
