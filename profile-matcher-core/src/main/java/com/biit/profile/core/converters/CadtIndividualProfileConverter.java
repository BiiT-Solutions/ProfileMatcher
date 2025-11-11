package com.biit.profile.core.converters;

/*-
 * #%L
 * Profile Matcher (Core)
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

import com.biit.profile.core.converters.models.CadtIndividualProfileConverterRequest;
import com.biit.profile.core.models.CadtIndividualProfileDTO;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CadtIndividualProfileConverter extends ElementConverter<CadtIndividualProfile, CadtIndividualProfileDTO,
        CadtIndividualProfileConverterRequest> {


    @Override
    protected CadtIndividualProfileDTO convertElement(CadtIndividualProfileConverterRequest from) {
        final CadtIndividualProfileDTO profileDTO = new CadtIndividualProfileDTO();
        BeanUtils.copyProperties(from.getEntity(), profileDTO);
        profileDTO.setSession(from.getEntity().getSession());
        return profileDTO;
    }

    @Override
    public CadtIndividualProfile reverse(CadtIndividualProfileDTO from) {
        if (from == null) {
            return null;
        }
        final CadtIndividualProfile profile = new CadtIndividualProfile();
        BeanUtils.copyProperties(from, profile);
        profile.setSession(from.getSession());
        return profile;
    }
}
