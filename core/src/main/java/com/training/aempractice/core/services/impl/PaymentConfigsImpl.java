package com.training.aempractice.core.services.impl;

import com.training.aempractice.core.services.PaymentConfigs;
import com.training.aempractice.core.services.Configrations.SampleConfigrations;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = PaymentConfigs.class, immediate = true)
@Designate(ocd= SampleConfigrations.class)
public class PaymentConfigsImpl implements PaymentConfigs {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentConfigsImpl.class);

    private  String paymenturl;

    @Activate
    protected void activate(SampleConfigrations paymenturl)
    {
        log.info("Activating PaymentConfigs service");
        this.paymenturl=paymenturl.paymentUrl();
        log.debug("Payment URL loaded from config");
    }


    @Override
    public String getPaymentUrl() {
        return paymenturl;
    }

    @Deactivate
    protected void deactivate() {
        log.info("Deactivating PaymentConfigs service");
    }
}
