package bot.links;

import edu.java.bot.links.Link;
import edu.java.bot.links.UserIdLinks;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserIdLinksTest {
    private static final String GIT_HUB = "https://github.com";
    private static final String STACK_OVER_FLOW = "https://stackoverflow.com/";
    private static final Long userId = 1L;

    private final UserIdLinks userIdLinks = new UserIdLinks();
    @Test
    void testGetLinkIfPossibleMethodIfCommandIsCorrect(){
        String commandText = "/track %s".formatted(GIT_HUB);

        String result = UserIdLinks.getLinkIfPossible(commandText);

        assertThat(result).isEqualTo(GIT_HUB);
    }

    @ParameterizedTest
    @MethodSource("getIncorrectCommands")
    @DisplayName("тестирование метода getLinkIfPossible если комманда некорректна")
    void testGetLinkIfPossibleMethodIfCommandIsNotCorrect(String commandText){
        String result = UserIdLinks.getLinkIfPossible(commandText);

        assertThat(result).isEqualTo("");
    }

    private static Stream<Arguments> getIncorrectCommands(){
        return Stream.of(
            Arguments.of("/track"),
            Arguments.of("/track мусор мусор")
        );
    }

    @Test
    void testAddTrackLink(){
        Link gitHubLink = new Link(URI.create(GIT_HUB), LocalDateTime.MIN);
        Link stackOverFlow = new Link(URI.create(STACK_OVER_FLOW), LocalDateTime.MIN.plusDays(1L));

        userIdLinks.addTrackLink(gitHubLink, userId);
        userIdLinks.addTrackLink(stackOverFlow, userId);

        Set<Link> userLinkSet = Set.of(gitHubLink, stackOverFlow);
        Set<Link> result = userIdLinks.getTrackList(userId);

        assertThat(userLinkSet.containsAll(result)).isTrue();

        userIdLinks.unTrackLink(URI.create(GIT_HUB), userId);
        userIdLinks.unTrackLink(URI.create(STACK_OVER_FLOW), userId);
    }

    @Test
    void testUnTrackLink(){
        Link gitHubLink = new Link(URI.create(GIT_HUB), LocalDateTime.MIN);
        Link stackOverFlow = new Link(URI.create(STACK_OVER_FLOW), LocalDateTime.MIN.plusDays(1L));

        userIdLinks.addTrackLink(gitHubLink, userId);
        userIdLinks.addTrackLink(stackOverFlow, userId);

        userIdLinks.unTrackLink(URI.create(GIT_HUB), userId);
        userIdLinks.unTrackLink(URI.create(STACK_OVER_FLOW), userId);

        Set<Link> result = userIdLinks.getTrackList(userId);

        assertThat(result.isEmpty()).isTrue();
    }
}
