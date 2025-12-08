package com.training.aempractice.core.services.impl;

import org.osgi.service.component.annotations.Component;

@Component(service = GreetingService.class, immediate = true)
public class GreetingServiceImpl  implements  GreetingService

{
    @Override
    public String getGreeting(String name) {
        return "Hello" +name +", welcome to AEM OSGi services!";
    }
}
