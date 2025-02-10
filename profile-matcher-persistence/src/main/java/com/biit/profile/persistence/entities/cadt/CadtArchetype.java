package com.biit.profile.persistence.entities.cadt;

public enum CadtArchetype {
    RECEPTIVE("receptive"),
    INNOVATOR("innovator"),
    VISIONARY("visionary"),
    STRATEGIST("strategist"),
    BANKER("banker"),
    TRADESMAN("tradesman"),
    SCIENTIST("scientist"),
    LEADER("leader");

    private final String tag;


    CadtArchetype(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static CadtArchetype fromTag(String tag) {
        for (CadtArchetype archetype : CadtArchetype.values()) {
            if (archetype.getTag().equalsIgnoreCase(tag)) {
                return archetype;
            }
        }
        return null;
    }
}
