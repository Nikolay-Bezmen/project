package edu.java.clients.stackoverflow;

import edu.java.dto.stackoverflow.StackOverflowAnswer;
import edu.java.dto.stackoverflow.StackOverflowQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StackOverflowClient {
    private final WebClient webClient;
    private static final String END_URI = "/questions/{id}/answers?site=stackoverflow&filter=withbody";

    @Autowired
    public StackOverflowClient(@Qualifier("stackOverflowWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<StackOverflowQuestion> findQuestionById(int id) {
        return webClient.get()
            .uri(END_URI, id)
            .retrieve()
            .bodyToMono(StackOverflowQuestion.class);
    }

    public Mono<StackOverflowAnswer> findAnswersByQuestionId(@PathVariable int id) {
        return webClient.get()
            .uri(END_URI, id)
            .retrieve()
            .bodyToMono(StackOverflowAnswer.class);
    }
}
