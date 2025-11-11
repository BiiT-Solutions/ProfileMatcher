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
import com.biit.profile.core.metaviewer.Collection;
import com.biit.profile.core.metaviewer.ObjectMapperFactory;
import com.biit.profile.core.providers.MetaviewerProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metaviewer")
public class MetaviewerServices {

    private final CadtIndividualProfileController cadtIndividualProfileController;
    private final MetaviewerProvider metaviewerProvider;

    public MetaviewerServices(CadtIndividualProfileController cadtIndividualProfileController,
                              MetaviewerProvider metaviewerProvider) {
        this.cadtIndividualProfileController = cadtIndividualProfileController;
        this.metaviewerProvider = metaviewerProvider;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT result as json.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection createJson(Authentication authentication, HttpServletResponse response) {
        return metaviewerProvider.getCollection();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT result as xml.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public String createXml(Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        return ObjectMapperFactory.generateXml(metaviewerProvider.getCollection());
    }

    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Regenerates CADT result as and json and stores it to a file.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/refresh", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public void refresh(Authentication authentication, HttpServletResponse response) {
        cadtIndividualProfileController.updateFromFactManager();
        metaviewerProvider.populateSamplesFolder();
    }

}
