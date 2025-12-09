package com.training.aempractice.core.services.impl;

import com.training.aempractice.core.services.MessageService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceRanking;

@Component(service = MessageService.class, immediate = true)
@ServiceRanking(value = 1000)
public class MessageServiceImpl2  implements MessageService {

    @Override
    public String getMessage(String name) {
        return "Hello I am from Service2";
    }
}
