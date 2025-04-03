package com.biit.profile.core.handlers;

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
