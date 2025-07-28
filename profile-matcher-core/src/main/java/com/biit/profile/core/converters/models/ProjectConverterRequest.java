package com.biit.profile.core.converters.models;

import com.biit.profile.persistence.entities.Project;
import com.biit.server.converters.models.ConverterRequest;

public class ProjectConverterRequest extends ConverterRequest<Project> {
    public ProjectConverterRequest(Project project) {
        super(project);
    }
}
