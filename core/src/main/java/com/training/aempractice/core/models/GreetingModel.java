package com.training.aempractice.core.models;


import com.training.aempractice.core.services.GreetingService;
import com.training.aempractice.core.services.MessageService;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;

@Getter
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GreetingModel {

    @ValueMapValue
    private String nameOfPerson;

    @ValueMapValue
    private int ageOfPerson;

    @ValueMapValue
    private String descriptionOfPerson;

    /*public String getNameOfPerson() {
        return nameOfPerson;
    }

    public int getAgeOfPerson() {
        return ageOfPerson;
    }

    public String getDescriptionOfPerson() {
        return descriptionOfPerson;
    }*/


    @OSGiService
    GreetingService greetingService;

    @OSGiService
    MessageService messageService;

    public String getGreetingService()
    {
        return greetingService.getGreeting("Vamsi");
    }

    public String getMessageService()
    {
        return messageService.getMessage("From Service");
    }




}
