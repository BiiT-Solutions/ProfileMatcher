package com.biit.profile.core.kafka;


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
