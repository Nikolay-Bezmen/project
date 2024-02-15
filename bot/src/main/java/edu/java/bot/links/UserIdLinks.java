package edu.java.bot.links;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserIdLinks {
    private UserIdLinks() {
    }

    private static final Map<Long, Set<Link>> TRACK_SET = new HashMap<>();
    private static final Map<Long, Map<URI, Link>> TRACK_MAP = new HashMap<>();

    public static Set<Link> getTrackList(Long userId) {
        if (!TRACK_SET.containsKey(userId)) {
            TRACK_SET.put(userId, new HashSet<>());
            TRACK_MAP.put(userId, new HashMap<>());

        }
        return new HashSet<>(TRACK_SET.get(userId));

    }

    public static boolean addTrackLink(Link link, Long userId) {
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

    public static boolean unTrackLink(URI uri, Long userId) {
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

    private static Set<Link> getTrackSetByUserId(Long userId) {
        if (!TRACK_SET.containsKey(userId)) {
            TRACK_SET.put(userId, new HashSet<>());
        }

        return TRACK_SET.get(userId);
    }

    private static Map<URI, Link> getTrackMapByUserId(Long userId) {
        if (!TRACK_MAP.containsKey(userId)) {
            TRACK_MAP.put(userId, new HashMap<>());
        }

        return TRACK_MAP.get(userId);
    }
}
