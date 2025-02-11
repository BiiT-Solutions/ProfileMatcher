package com.biit.profile.rest.api;

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
        return getController().findByCompetencesIn(competences, threshold, authentication.getName());
    }


}
