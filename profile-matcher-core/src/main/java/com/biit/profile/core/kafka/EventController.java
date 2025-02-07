package com.biit.profile.core.kafka;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.biit.kafka.logger.EventsLogger;
import com.biit.profile.core.controllers.CadtIndividualProfileController;
import com.biit.profile.core.providers.CadtIndividualProfileProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;


/**
 * Subscribes to the EventListener to get any event, and handles it.
 */
@ConditionalOnExpression("${spring.kafka.enabled:false}")
@Controller
public class EventController {

    public static final String ALLOWED_FACT_TYPE = "DroolsResultForm";

    private final CadtIndividualProfileController cadtIndividualProfileController;

    public EventController(CadtIndividualProfileController cadtIndividualProfileController) {
        this.cadtIndividualProfileController = cadtIndividualProfileController;
    }


    public void eventHandler(Event event, String groupId, String key, int partition, String topic, long timeStamp) {
        EventsLogger.debug(this.getClass(), "Received event '{}' on topic '{}', group '{}', key '{}', partition '{}' at '{}'",
                event, topic, groupId, key, partition, LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),
                        TimeZone.getDefault().toZoneId()));

        try {
            if (event.getCustomProperties() != null) {
                if (!Objects.equals(event.getCustomProperty(EventCustomProperties.FACT_TYPE), ALLOWED_FACT_TYPE)) {
                    EventsLogger.debug(this.getClass(), "Event is not a form. Ignored.");
                    return;
                }
                if (!Objects.equals(event.getTag(), CadtIndividualProfileProvider.FORM_NAME)) {
                    EventsLogger.debug(this.getClass(), "Event is a different form. Ignored");
                    return;
                }
            }

            final DroolsSubmittedForm droolsForm = getDroolsForm(event);

            final String createdBy = event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag()) != null
                    ? event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag())
                    : event.getCreatedBy();

            EventsLogger.info(this.getClass(), "Received new drools form from '{}'", createdBy);

            cadtIndividualProfileController.newFormReceived(droolsForm);
            //cadtScoreController.newFormReceived(droolsForm);

        } catch (JsonProcessingException e) {
            EventsLogger.severe(this.getClass(), "Event cannot be parsed!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        } catch (Exception e) {
            EventsLogger.severe(this.getClass(), "Invalid event received!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }

    private DroolsSubmittedForm getDroolsForm(Event event) throws JsonProcessingException {
        return event.getEntity(DroolsSubmittedForm.class);
    }
}
