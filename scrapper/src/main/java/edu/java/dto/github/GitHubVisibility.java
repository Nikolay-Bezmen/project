package edu.java.dto.github;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GitHubVisibility {
    PUBLIC,
    PRIVATE;

    @JsonCreator
    public static GitHubVisibility getVisibility(String visibility) {
        return PRIVATE.name().equalsIgnoreCase(visibility)
            ? PRIVATE
            : PUBLIC;
    }
}
