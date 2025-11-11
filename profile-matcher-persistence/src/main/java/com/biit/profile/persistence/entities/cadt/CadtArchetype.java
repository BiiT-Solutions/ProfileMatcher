package com.biit.profile.persistence.entities.cadt;

/*-
 * #%L
 * Profile Matcher (Persistence)
 * %%
 * Copyright (C) 2024 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
