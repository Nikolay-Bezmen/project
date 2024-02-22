package edu.java.dto.github;

import java.time.OffsetDateTime;

public record GitHubRepositoryActivity(
    long id,
    GitHubOwner actor,
    OffsetDateTime timestamp,
    String ref,
    String activityType
) {
}
