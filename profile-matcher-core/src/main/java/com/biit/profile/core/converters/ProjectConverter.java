package com.biit.profile.core.converters;

import com.biit.profile.core.converters.models.ProjectConverterRequest;
import com.biit.profile.core.models.ProjectDTO;
import com.biit.profile.persistence.entities.Project;
import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProjectConverter extends ElementConverter<Project, ProjectDTO, ProjectConverterRequest> {


    @Override
    protected ProjectDTO convertElement(ProjectConverterRequest from) {
        if (from.getEntity() == null) {
            return null;
        }
        final ProjectDTO projectDTO = new ProjectDTO();
        BeanUtils.copyProperties(from.getEntity(), projectDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        return projectDTO;
    }


    @Override
    public Project reverse(ProjectDTO to) {
        if (to == null) {
            return null;
        }
        final Project project = new Project();
        BeanUtils.copyProperties(to, project, ConverterUtils.getNullPropertyNames(to));
        return project;
    }
}
