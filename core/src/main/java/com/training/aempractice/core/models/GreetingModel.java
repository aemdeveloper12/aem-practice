package com.training.aempractice.core.models;


import com.training.aempractice.core.services.impl.GreetingService;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import org.apache.sling.api.resource.Resource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GreetingModel {

    @ValueMapValue
    private String nameOfPerson;

    @ValueMapValue
    private int ageOfPerson;

    @ValueMapValue
    private String descriptionOfPerson;

    public String getNameOfPerson() {
        return nameOfPerson;
    }

    public int getAgeOfPerson() {
        return ageOfPerson;
    }

    public String getDescriptionOfPerson() {
        return descriptionOfPerson;
    }
    @OSGiService
    GreetingService greetingService;

    public String getGreetingService()
    {
        return greetingService.getGreeting("Vamsi");
    }




}
