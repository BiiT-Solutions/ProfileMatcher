package com.biit.profile.core.handlers;

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

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.biit.kafka.logger.EventsLogger;
import com.biit.profile.core.providers.ICadtController;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class CadtHandler {

    private final ICadtController controller;


    protected CadtHandler(ICadtController controller) {
        this.controller = controller;
    }


    protected DroolsSubmittedForm getDroolsForm(Event event) {
        return event.getEntity(DroolsSubmittedForm.class);
    }


    public void processEvent(Event event, String allowedForm) {
        if (!Objects.equals(event.getTag(), allowedForm)) {
            EventsLogger.debug(this.getClass(), "Event is from a different form. Ignored");
            return;
        }

        EventsLogger.info(this.getClass(), "Processing profile from event.");
        try {
            final DroolsSubmittedForm droolsForm = getDroolsForm(event);

            final String createdBy = event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag()) != null
                    ? event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag())
                    : event.getCreatedBy();

            EventsLogger.info(this.getClass(), "Received new drools form from '{}'", createdBy);
            droolsForm.setSubmittedAt(LocalDateTime.now());
            droolsForm.setSubmittedBy(createdBy);
            droolsForm.setText(droolsForm.getTag());

            controller.newProfileReceived(droolsForm, event.getSessionId());
        } catch (Exception e) {
            EventsLogger.severe(this.getClass(), "Invalid event received!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }
}
