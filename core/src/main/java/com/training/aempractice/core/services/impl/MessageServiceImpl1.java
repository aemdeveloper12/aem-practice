package com.training.aempractice.core.services.impl;

import com.training.aempractice.core.services.MessageService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceRanking;


@Component(service = MessageService.class, immediate = true)
@ServiceRanking(value = 999)
public class MessageServiceImpl1  implements MessageService{

    @Override
    public String getMessage(String name) {
        return "Hello I am from Service1";
    }


}
