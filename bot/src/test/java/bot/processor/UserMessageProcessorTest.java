package bot.processor;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.links.UserIdLinks;
import edu.java.bot.processor.UserMessageProcessorImpl;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.stereotype.Component;
import static edu.java.bot.commands.StartCommand.START_WORK;
import static edu.java.bot.processor.UserMessageProcessorImpl.SUCH_COMMAND_IS_NOT_EXIST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@Component
@ExtendWith(MockitoExtension.class)
public class UserMessageProcessorTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Autowired
    private List<Command> commandList;
    @InjectMocks
    private UserMessageProcessorImpl userMessageProcessorImpl;

    @Test
    @DisplayName("тестирование метода commands который должен вернуть все доступные комманды")
    void testCommands() {
        assertThat(userMessageProcessorImpl.commands()).usingRecursiveComparison().isEqualTo(commandList);
    }

    @Test
    @DisplayName("тестирование если комманда существует")
    void testProcessWithExistingCommand(){
        doReturn(message).when(update).message();
        doReturn("/start").when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(123L).when(chat).id();

        List<Command> commands = new ArrayList<>();
        commands.add(new StartCommand());
        userMessageProcessorImpl = new UserMessageProcessorImpl(commands);
        SendMessage correctSendMessage = new SendMessage(message.chat().id(), START_WORK);
        SendMessage sendMessage = userMessageProcessorImpl.process(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }

    @Test
    @DisplayName("тестирование если комманда существует")
    void testProcessWithNonExistingCommand(){
        doReturn(message).when(update).message();
        doReturn("/unknown").when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(123L).when(chat).id();

        List<Command> commands = new ArrayList<>();
        userMessageProcessorImpl = new UserMessageProcessorImpl(commands);
        SendMessage correctSendMessage = new SendMessage(message.chat().id(), SUCH_COMMAND_IS_NOT_EXIST);
        SendMessage sendMessage = userMessageProcessorImpl.process(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }
}
