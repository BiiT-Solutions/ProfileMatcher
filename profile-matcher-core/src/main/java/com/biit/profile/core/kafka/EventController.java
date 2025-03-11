package com.biit.profile.core.kafka;

import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.biit.kafka.events.EventSubject;
import com.biit.kafka.logger.EventsLogger;
import com.biit.profile.core.handlers.ProfileCadtHandler;
import com.biit.profile.core.handlers.UserCadtHandler;
import com.biit.profile.core.providers.CadtIndividualProfileProvider;
import com.biit.profile.persistence.entities.Profile;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserCadtHandler userCadtHandler;
    private final ProfileCadtHandler profileCadtHandler;

    public EventController(@Autowired(required = false) EventConsumerListener eventListener, UserCadtHandler userCadtHandler, ProfileCadtHandler profileCadtHandler) {
        this.userCadtHandler = userCadtHandler;
        this.profileCadtHandler = profileCadtHandler;

        //Listen to topic
        if (eventListener != null) {
            eventListener.addListener((event, offset, groupId, key, partition, topic, timeStamp) ->
                    eventHandler(event, groupId, key, partition, topic, timeStamp));
        }
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
            }

            if (Objects.equals(event.getSubject(), EventSubject.CREATED.name())) {
                EventsLogger.info(this.getClass(), "Ignoring events not for creation.");
            }

            EventsLogger.info(this.getClass(), "Received profile form event.");

            //Is it a cadt from a user?
            userCadtHandler.processEvent(event, CadtIndividualProfileProvider.FORM_NAME);

            //Is it a cadt from a vacancy?
            profileCadtHandler.processEvent(event, Profile.CADT_PROFILE_FORM);


        } catch (Exception e) {
            EventsLogger.severe(this.getClass(), "Invalid event received!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }

}
