package com.chavau.univ_angers.univemarge.database.entities;

import java.util.HashMap;
import java.util.Map;

public enum StatutPresence {
    INCONNU(-1),
    ABSENT(0),
    EXCUSE(1),
    PRESENT(2),
    HORS_CRENEAU(4),
    RETARD(42); // TODO: Feature

    private final int value;

    private StatutPresence(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, StatutPresence> intToTypeMap = new HashMap<>();

    static {
        for (StatutPresence type : StatutPresence.values()) {
            intToTypeMap.put(type.value, type);
        }
    }

    public static StatutPresence fromInt(int i) {
        StatutPresence type = intToTypeMap.get(i);
        if (type == null)
            return StatutPresence.INCONNU;
        return type;
    }
}