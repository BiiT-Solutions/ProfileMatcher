package com.biit.profile.rest.api;

/*-
 * #%L
 * Profile Matcher (Rest)
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

import com.biit.profile.core.controllers.CadtIndividualProfileController;
import com.biit.profile.core.converters.CadtIndividualProfileConverter;
import com.biit.profile.core.converters.models.CadtIndividualProfileConverterRequest;
import com.biit.profile.core.models.CadtIndividualProfileDTO;
import com.biit.profile.core.providers.CadtIndividualProfileProvider;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.repositories.CadtIndividualProfileRepository;
import com.biit.server.rest.ElementServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/individual-reports")
public class CadtIndividualProfileServices extends ElementServices<CadtIndividualProfile, Long, CadtIndividualProfileDTO,
        CadtIndividualProfileRepository, CadtIndividualProfileProvider, CadtIndividualProfileConverterRequest, CadtIndividualProfileConverter,
        CadtIndividualProfileController> {


    protected CadtIndividualProfileServices(CadtIndividualProfileController controller) {
        super(controller);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets any individual profile that matches a selection of competences.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/competences/{threshold}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CadtIndividualProfileDTO> getByCompetences(
            @Parameter(name = "List of matching competences", required = false) @RequestParam(value = "competence", required = false) List<String> competences,
            @Parameter(description = "Minimum competence number to match", required = true) @PathVariable("threshold") int threshold,
            Authentication authentication, HttpServletRequest request) {
        return getController().findByCompetenceTagsIn(competences, threshold, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a list of candidates that matches a profile.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/profiles/{profileId}/thresholds/{threshold}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CadtIndividualProfileDTO> getByprofile(
            @Parameter(description = "Profile Id", required = true) @PathVariable("profileId") long profileId,
            @Parameter(description = "Minimum competence number to match", required = true) @PathVariable("threshold") int threshold,
            Authentication authentication, HttpServletRequest request) {
        return getController().findByProfile(profileId, threshold, authentication.getName());
    }


}
