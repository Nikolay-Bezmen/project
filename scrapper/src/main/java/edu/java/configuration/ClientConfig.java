package edu.java.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
@Component
@RequiredArgsConstructor
public class ClientConfig {
    private final ApplicationConfig applicationConfig;

    @Bean
    public WebClient githubWebClient() {
        return getNewWebClient(applicationConfig.urls().githubBaseUrl());
    }

    @Bean
    public WebClient stackOverflowWebClient() {
        return getNewWebClient(applicationConfig.urls().stackOverflowBaseUrl());
    }

    private WebClient getNewWebClient(String baseUrl) {
        final var tcpClient = TcpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) applicationConfig.scheduler().interval().getSeconds())
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler((int) applicationConfig.scheduler().interval()
                    .getSeconds()));
                connection.addHandlerLast(new WriteTimeoutHandler((int) applicationConfig.scheduler().interval()
                    .getSeconds()));
            });

        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
            .build();
    }
}
