package com.training.aempractice.core.services.impl;

import com.training.aempractice.core.services.MessageService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = MessageService.class, immediate = true)
@ServiceRanking(value = 1000)
public class MessageServiceImpl2  implements MessageService {


    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl2.class);

    @Override
    public String getMessage(String name) {

        log.info("MessageServiceImpl2 is being used");
        return "Hello I am from Service2";
    }
}
