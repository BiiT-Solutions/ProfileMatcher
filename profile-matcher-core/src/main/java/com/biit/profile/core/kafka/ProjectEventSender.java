package com.biit.profile.core.kafka;

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


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.profile.core.models.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sends an event when the controller of Profile performs any CRUD operation.
 * EVENT_TYPE will define the FACT_TYPE custom property on the event.
 */
@Component
public class ProjectEventSender extends EventSender<ProjectDTO> {

    private static final String MY_ENTITY_EVENT_TYPE = "Project";

    public static final String TAG = "ProfileMatcher";

    public ProjectEventSender(@Autowired(required = false) KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, TAG, MY_ENTITY_EVENT_TYPE);
    }
}
