package com.training.aempractice.core.services.impl;

import com.training.aempractice.core.services.PaymentService;
import configrations.PaymentConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Designate(ocd= PaymentConfig.class)
@Component(service = PaymentService.class, immediate = true)
public class PaymentServiceImpl  implements PaymentService {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    private String externlPaymentUrl;

    @Activate
    protected  void activate(PaymentConfig paymentConfig)
    {
        log.info("Activating paymentConfigs service");
        externlPaymentUrl= paymentConfig.paymentUrl();

    }

    @Modified
    protected void modified()
    {
        log.info(" Service is Modified");
    }

    @Override
    public String paymentUrl() {
        return externlPaymentUrl;
    }
}
