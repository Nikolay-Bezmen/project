package edu.java.dto.stackoverflow;

public record StackOverflowOwner(
    int accountId,
    int userId,
    String displayName
){}
