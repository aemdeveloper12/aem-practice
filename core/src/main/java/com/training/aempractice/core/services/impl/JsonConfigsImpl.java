package com.training.aempractice.core.services.impl;

import com.training.aempractice.core.services.Configrations.SampleConfigrations;
import com.training.aempractice.core.services.JsonConfigs;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JsonConfigs.class, immediate = true)
@Designate(ocd = SampleConfigrations.class)
public class JsonConfigsImpl implements JsonConfigs {

    private static final Logger log =
            LoggerFactory.getLogger(JsonConfigsImpl.class);

    private String jsonurl;

    @Activate
    protected void activate(SampleConfigrations sampleConfigrations) {
        log.info("Activating jsonConfigs service");
        jsonurl = sampleConfigrations.jsonUrl();
        log.debug("Payment URL loaded from config");
    }

    @Override
    public String getjsonUrl() {
        return jsonurl;
    }

    @Deactivate
    protected void deactivate() {
        log.info("Deactivating PaymentConfigs service");
    }
}
