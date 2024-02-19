package edu.java.bot.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UrlValidationUtils {
    private UrlValidationUtils() {
    }

    private static final int CORRECT_CODE_OF_RESPONSE = 200;
    /**
     * если выбрасывается IllegalArgumentException => нарушен стандарт RFC 2396 => не является ссылкой
     * если выбрасываются все остальные exceptions => проблемы связанные с получением доступа к сайту
     */

    public static final String LINK_IS_VALID = "ссылка валидна";
    public static final String INCORRECT_URL = "введённая вами ссылка невалидна";
    public static final String PROBLEMS_WITH_ACCESS = "в данный момент ссылку \"%s\" невозможно "
        + "отслеживать в связи с проблемами с доступом к сайту";

    public static String validateLink(String link) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(link))
                .GET()
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int responseCode = response.statusCode();

            if (responseCode == CORRECT_CODE_OF_RESPONSE) {
                return LINK_IS_VALID;
            } else {
                throw new Exception();
            }
        } catch (IllegalArgumentException e) {
            return INCORRECT_URL;
        } catch (Exception e) {
            return PROBLEMS_WITH_ACCESS;
        }
    }
}
