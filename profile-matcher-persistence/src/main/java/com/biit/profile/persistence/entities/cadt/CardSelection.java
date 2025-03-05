package com.biit.profile.persistence.entities.cadt;

public enum CardSelection {
    FIRST,
    SECOND,
    SHADOWED,
    DISCARDED,
    SELECTED;

    public boolean isSelected() {
        return this == CardSelection.FIRST || this == CardSelection.SECOND;
    }
}
