package com.biit.profile.rest.api;


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
