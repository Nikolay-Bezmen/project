package edu.java.bot.links;

import java.net.URI;
import java.time.LocalDateTime;

public class Link {
    private URI uri;
    private LocalDateTime startTrackingDateTime;

    public Link(URI uri, LocalDateTime startTrackingDateTime) {
        this.uri = uri;
        this.startTrackingDateTime = startTrackingDateTime;
    }

    public String getInfo() {
        return "%s, %s %s".formatted(uri, "отслеживается с ", startTrackingDateTime.toString());
    }

    public URI url() {
        return uri;
    }

    public LocalDateTime startTrackingDateTime() {
        return startTrackingDateTime;
    }

}
