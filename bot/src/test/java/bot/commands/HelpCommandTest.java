package bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.HelpCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.commands.HelpCommand.COMMAND_TEXT;
import static edu.java.bot.commands.HelpCommand.DESCRIPTION_TEXT;
import static org.mockito.Mockito.doReturn;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class HelpCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @InjectMocks
    private HelpCommand helpCommand;

    @Test
    @DisplayName("тестирование метода description")
    void testDescription(){
        String description = helpCommand.description();
        String correctDescription = DESCRIPTION_TEXT;

        assertThat(description).isEqualTo(correctDescription);
    }


    @Test
    @DisplayName("тестирование метода command")
    void testCommand(){
        String command = helpCommand.command();
        String correctCommand = COMMAND_TEXT;

        assertThat(command).isEqualTo(correctCommand);
    }

    @Test
    @DisplayName("тестирование метода handle")
    void testHandle(){
        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(9910481L).when(chat).id();

        String correctMessageText = "/help - выводит окно с командами\n\n" +
            "/list - показывает список отслеживаемых ссылок\n\n" +
            "/start - регистрирует пользователя\n\n" +
            "/track - начинает отслеживание ссылки." +
            " Чтобы начать отслеживание ссылки введите комманду \"/track ваша_ссылка\"\n\n" +
            "/untrack - прекращает отслеживание ссылки." +
            " Чтобы закончить отслеживание ссылки введите комманду \"/untrack ваша_ссылка\"\n\n";

        SendMessage sendMessage = helpCommand.handle(update);
        SendMessage correctSendMessage = new SendMessage(chat.id(), correctMessageText);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(correctSendMessage.toWebhookResponse());
    }
}
