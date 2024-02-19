package bot.utils;

import edu.java.bot.utils.UrlValidationUtils;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.UrlValidationUtils.INCORRECT_URL;
import static edu.java.bot.utils.UrlValidationUtils.LINK_IS_VALID;
import static edu.java.bot.utils.UrlValidationUtils.PROBLEMS_WITH_ACCESS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UrlValidationUtilsTest {
    @Test
    void testIfUriIsCorrect(){
        String uri = "https://github.com";

        String result = UrlValidationUtils.validateLink(uri);

        assertThat(result).isEqualTo(LINK_IS_VALID);
    }

    @Test
    void testIfLinkHasProblemWithAccess(){
        String uri = "https://github.com/мусор";

        String result = UrlValidationUtils.validateLink(uri);

        assertThat(result).isEqualTo(PROBLEMS_WITH_ACCESS);
    }

    @Test
    void testIfLinkIsNotValid(){
        String uri = "невалидная ссылка";

        String result = UrlValidationUtils.validateLink(uri);

        assertThat(result).isEqualTo(INCORRECT_URL);
    }
}
