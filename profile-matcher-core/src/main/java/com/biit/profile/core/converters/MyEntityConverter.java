package com.biit.profile.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.profile.core.converters.models.MyEntityConverterRequest;
import com.biit.profile.core.models.MyEntityDTO;
import com.biit.profile.persistence.entities.MyEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MyEntityConverter extends ElementConverter<MyEntity, MyEntityDTO, MyEntityConverterRequest> {


    @Override
    protected MyEntityDTO convertElement(MyEntityConverterRequest from) {
        final MyEntityDTO myEntityDTO = new MyEntityDTO();
        BeanUtils.copyProperties(from.getEntity(), myEntityDTO);
        return myEntityDTO;
    }

    @Override
    public MyEntity reverse(MyEntityDTO to) {
        if (to == null) {
            return null;
        }
        final MyEntity myEntity = new MyEntity();
        BeanUtils.copyProperties(to, myEntity);
        return myEntity;
    }
}
