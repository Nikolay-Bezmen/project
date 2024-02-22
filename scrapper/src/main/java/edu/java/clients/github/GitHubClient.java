package edu.java.clients.github;

import edu.java.dto.github.GitHubRepositoryActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GitHubClient {
    private final WebClient webClient;

    @Autowired
    public GitHubClient(@Qualifier("githubWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<GitHubRepositoryActivity> fetchQuestionInfo(String ownerName, String repoName) {
        return webClient
            .get()
            .uri("/repos/{owner}/{repoName}/activity", ownerName, repoName)
            .retrieve()
            .bodyToMono(GitHubRepositoryActivity.class);
    }
}
