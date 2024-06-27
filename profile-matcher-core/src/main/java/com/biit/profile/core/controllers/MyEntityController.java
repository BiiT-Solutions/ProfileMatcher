package com.biit.profile.core.controllers;


import com.biit.profile.core.converters.MyEntityConverter;
import com.biit.profile.core.converters.models.MyEntityConverterRequest;
import com.biit.profile.core.exceptions.MyEntityNotFoundException;
import com.biit.profile.core.kafka.MyEntityEventSender;
import com.biit.profile.core.providers.MyEntityProvider;
import com.biit.profile.core.models.MyEntityDTO;
import com.biit.profile.persistence.entities.MyEntity;
import com.biit.profile.persistence.repositories.MyEntityRepository;
import com.biit.kafka.controllers.KafkaElementController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MyEntityController extends KafkaElementController<MyEntity, Long, MyEntityDTO, MyEntityRepository,
        MyEntityProvider, MyEntityConverterRequest, MyEntityConverter> {

    @Autowired
    protected MyEntityController(MyEntityProvider provider, MyEntityConverter converter, MyEntityEventSender eventSender) {
        super(provider, converter, eventSender);
    }

    @Override
    protected MyEntityConverterRequest createConverterRequest(MyEntity myEntity) {
        return new MyEntityConverterRequest(myEntity);
    }

    public MyEntityDTO getByName(String name) {
        return getConverter().convert(new MyEntityConverterRequest(getProvider().findByName(name).orElseThrow(() ->
                new MyEntityNotFoundException(this.getClass(),
                        "No MyEntity with name '" + name + "' found on the system."))));
    }
}
