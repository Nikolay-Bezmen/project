package edu.java.bot.links;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class UserIdLinks {
    public UserIdLinks() {
        trackMap = new HashMap<>();
        trackSet = new HashMap<>();
    }

    private final Map<Long, Set<Link>> trackSet;
    private final Map<Long, Map<URI, Link>> trackMap;

    public Set<Link> getTrackList(Long userId) {
        if (!trackSet.containsKey(userId)) {
            trackSet.put(userId, new HashSet<>());
            trackMap.put(userId, new HashMap<>());

        }
        return new HashSet<>(trackSet.get(userId));

    }

    public boolean addTrackLink(Link link, Long userId) {
        var userTrackSet = getTrackSetByUserId(userId);
        var userTrackMap = getTrackMapByUserId(userId);

        if (userTrackMap.containsKey(link.url())) {
            return false;
        } else {
            userTrackMap.put(link.url(), link);
            userTrackSet.add(link);

            return true;
        }
    }

    public boolean unTrackLink(URI uri, Long userId) {
        var userTrackSet = getTrackSetByUserId(userId);
        var userTrackMap = getTrackMapByUserId(userId);

        if (userTrackMap.containsKey(uri)) {
            Link link = userTrackMap.get(uri);

            userTrackMap.remove(uri);
            userTrackSet.remove(link);

            return true;
        } else {
            return false;
        }
    }

    public static String getLinkIfPossible(String link) {
        String canonicalLink = new String(link);
        while (canonicalLink != canonicalLink.replaceAll("  ", " ")) {
            canonicalLink = canonicalLink.replaceAll("  ", " ");
        }

        String[] splitMessageBySpace = canonicalLink.split(" ");

        if (splitMessageBySpace.length != 2) {
            return "";
        } else {
            StringBuilder canonicalLinkSb = new StringBuilder(splitMessageBySpace[1]);
            int index = canonicalLinkSb.length() - 1;
            while (index >= 0 && canonicalLinkSb.charAt(index) == '/') {
                --index;
            }

            return canonicalLinkSb.substring(0, index + 1);
        }
    }

    public Set<Link> getTrackSetByUserId(Long userId) {
        if (!trackSet.containsKey(userId)) {
            trackSet.put(userId, new HashSet<>());
        }

        return trackSet.get(userId);
    }

    private Map<URI, Link> getTrackMapByUserId(Long userId) {
        if (!trackMap.containsKey(userId)) {
            trackMap.put(userId, new HashMap<>());
        }

        return trackMap.get(userId);
    }
}
