package bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.StartCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.commands.StartCommand.COMMAND_TEXT;
import static edu.java.bot.commands.StartCommand.DESCRIPTION_TEXT;
import static edu.java.bot.commands.StartCommand.START_WORK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class StartCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @InjectMocks
    private StartCommand startCommand;

    @Test
    @DisplayName("тестирование метода description")
    void testDescription(){
        String description = startCommand.description();
        String correctDescription = DESCRIPTION_TEXT;

        assertThat(description).isEqualTo(correctDescription);
    }


    @Test
    @DisplayName("тестирование метода command")
    void testCommand(){
        String command = startCommand.command();
        String correctCommand = COMMAND_TEXT;

        assertThat(command).isEqualTo(correctCommand);
    }

    @Test
    @DisplayName("тестирование метода handle")
    void testHandle(){
        Long userId = 1L;
        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(userId).when(chat).id();

        SendMessage correctSendMessage = new SendMessage(userId, START_WORK);
        SendMessage sendMessage = startCommand.handle(update);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }
}
