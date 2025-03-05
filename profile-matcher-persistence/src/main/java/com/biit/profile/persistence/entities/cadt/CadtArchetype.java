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

    private static final String ARCHETYPE_SUFFIX = "-archetype";

    private final String tag;


    CadtArchetype(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getAlternativeTag() {
        return tag + ARCHETYPE_SUFFIX;
    }

    public static CadtArchetype fromTag(String tag) {
        for (CadtArchetype archetype : CadtArchetype.values()) {
            if (archetype.getTag().equalsIgnoreCase(tag) || archetype.getAlternativeTag().equalsIgnoreCase(tag)) {
                return archetype;
            }
        }
        return null;
    }
}
