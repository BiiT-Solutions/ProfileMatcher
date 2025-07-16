package com.biit.profile.rest.api;


import com.biit.server.rest.SecurityService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("securityService")
public class ProfilesSecurityService extends SecurityService {

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

    public String getOrganizationAdminPrivilege() {
        return "PROFILEMATCHER_ORGANIZATION_ADMIN";
    }
}
