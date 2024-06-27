package com.biit.profile.rest.api;

import com.biit.profile.core.controllers.MyEntityController;
import com.biit.profile.core.converters.MyEntityConverter;
import com.biit.profile.core.converters.models.MyEntityConverterRequest;
import com.biit.profile.core.providers.MyEntityProvider;
import com.biit.profile.core.models.MyEntityDTO;
import com.biit.profile.persistence.entities.MyEntity;
import com.biit.profile.persistence.repositories.MyEntityRepository;
import com.biit.server.rest.ElementServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entities")
public class MyEntityServices extends ElementServices<MyEntity, Long, MyEntityDTO, MyEntityRepository,
        MyEntityProvider, MyEntityConverterRequest, MyEntityConverter, MyEntityController> {

    public MyEntityServices(MyEntityController controller) {
        super(controller);
    }
}
