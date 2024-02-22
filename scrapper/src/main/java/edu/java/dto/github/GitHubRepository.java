package edu.java.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubRepository(
    long id,
    String name,
    GitHubOwner owner,
    String htmlUrl,
    String description,
    long forksCount,
    String language,
    boolean allowForking,
    GitHubVisibility visibility,
    boolean archived,
    boolean disabled,
    @JsonProperty("created_at")
    OffsetDateTime createdAt,
    @JsonProperty("updated_at")
    OffsetDateTime updatedAt,
    @JsonProperty("pushed_at")
    OffsetDateTime pushedAt
) {
}
