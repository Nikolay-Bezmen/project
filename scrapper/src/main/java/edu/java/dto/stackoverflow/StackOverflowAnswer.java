package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowAnswer(
    List<ItemAnswer> item
) {
    public record ItemAnswer(
        boolean isAccepted,
        int answerId,
        @JsonProperty("creation_date")
        OffsetDateTime creationDate,
        OffsetDateTime lastActivityDate
    ) {
    }
}
