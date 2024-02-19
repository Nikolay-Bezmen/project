package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

public class HelpCommandTest {

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Mock
    private Command mockCommand1;

    @Mock
    private Command mockCommand2;
    @Mock
    private List<Command> commands;

    @InjectMocks
    private HelpCommand helpCommand;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test command method")
    void testCommand() {
        assertThat(helpCommand.command()).isEqualTo(HelpCommand.COMMAND_TEXT);
    }

    @Test
    @DisplayName("Test description method")
    void testDescription() {
        assertThat(helpCommand.description()).isEqualTo(HelpCommand.DESCRIPTION_TEXT);
    }

    @Test
    @DisplayName("Test handle method")
    void testHandle() {
        List<Command> commandsList = new ArrayList<>();
        commandsList.add(mockCommand1);
        commandsList.add(mockCommand2);

        doReturn("Command1").when(mockCommand1).command();
        doReturn("Description1").when(mockCommand1).description();
        doReturn("Command2").when(mockCommand2).command();
        doReturn("Description2").when(mockCommand2).description();

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(123L).when(chat).id();
        doReturn(commandsList.stream()).when(commands).stream();

        SendMessage sendMessage = helpCommand.handle(update);
        String expectedText = "Command1 - Description1\n\n" +
            "Command2 - Description2\n\n";
        SendMessage expectedSendMessage = new SendMessage(123L, expectedText);

        assertThat(sendMessage.toWebhookResponse()).isEqualTo(expectedSendMessage.toWebhookResponse());
//        assertThat(sendMessage.text()).isEqualTo(expectedSendMessage.text());
    }
}
