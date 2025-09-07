package com.redcraft86.oddsandends;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class TransientFlags {
    public static final String GENERATING_WORLD = "generating_world";

    private static final Set<String> activeFlags = ConcurrentHashMap.newKeySet();

    public static void addFlag(String flag) {
        activeFlags.add(flag);
    }

    public static void removeFlag(String flag) {
        activeFlags.remove(flag);
    }

    public static boolean hasFlag(String flag) {
        return activeFlags.contains(flag);
    }

    public static void clearAllFlags() {
        activeFlags.clear();
    }

    public static Set<String> getAllFlags() {
        return new HashSet<>(activeFlags);
    }

    public static void toggleFlag(String flag) {
        if (hasFlag(flag)) {
            removeFlag(flag);
        } else {
            addFlag(flag);
        }
    }
}
