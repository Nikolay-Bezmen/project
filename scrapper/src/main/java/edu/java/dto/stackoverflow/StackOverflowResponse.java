package edu.java.dto.stackoverflow;

import java.util.List;

public record StackOverflowResponse<T>(
    List<T> items,
    boolean hasMore,
    int quotaMax,
    int quotaRemaining
) {
}
