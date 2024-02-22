package edu.java.dto.stackoverflow;

import java.util.List;

public record StackOverflowQuestion(
    List<ItemQuestion> items
) {
    public record ItemQuestion(
        StackOverflowOwner owner,
        String title,
        int answerCount,
        boolean isAnswered
    ){
    }
}
