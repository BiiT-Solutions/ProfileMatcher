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
