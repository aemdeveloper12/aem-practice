package com.training.aempractice.core.services.impl;

import configrations.ExternalApiConfig;
import com.training.aempractice.core.services.ExternalAPIConfigsService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Designate(ocd=ExternalApiConfig.class)
@Component(service = ExternalAPIConfigsService.class, immediate = true)
public class ExternalAPIConfigsImpl implements ExternalAPIConfigsService {

    private final Logger log = LoggerFactory.getLogger(ExternalAPIConfigsImpl.class);

    private String externalApiUrl;

@Activate
    protected void activate(ExternalApiConfig externalApiConfig)
{
    log.info("Activating jsonConfigs service");
    externalApiUrl= externalApiConfig.externalApiUrl();
}


    @Override
    public String externalUrl() {
        return externalApiUrl;
    }
}
