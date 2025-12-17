package com.training.aempractice.core.models;


import com.training.aempractice.core.services.JsonConfigs;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ConfigViewModel {

    @Getter
    @ValueMapValue
    private String text;

    @OSGiService
   private JsonConfigs jsonConfigs;

    public String getJsonUrl()
    {
        return  jsonConfigs!= null ? jsonConfigs.getjsonUrl():"Config service not available";
    }



}
