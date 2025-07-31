package com.biit.profile.rest.api;


import com.biit.server.rest.SecurityService;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service("securityService")
public class ProfilesSecurityService extends SecurityService {

    public ProfilesSecurityService(List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProviders) {
        super(userOrganizationProviders);
    }

    @Override
    public String getViewerPrivilege() {
        return "PROFILEMATCHER_VIEWER";
    }

    @Override
    public String getAdminPrivilege() {
        return "PROFILEMATCHER_ADMIN";
    }

    @Override
    public String getEditorPrivilege() {
        return "PROFILEMATCHER_EDITOR";
    }

    @Override
    public String getOrganizationAdminPrivilege() {
        return "PROFILEMATCHER_ORGANIZATION_ADMIN";
    }
}
