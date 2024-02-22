package edu.java.dto.stackoverflow;

import edu.java.clients.stackoverflow.StackOverflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class StackOverFlowService {
    private final StackOverflowClient stackOverflowClient;

    public void fetchQuestionAndAnswers(@PathVariable int questionId) {
        StackOverflowQuestion questionResponse =
            stackOverflowClient.findQuestionById(questionId).block();
    }
}
