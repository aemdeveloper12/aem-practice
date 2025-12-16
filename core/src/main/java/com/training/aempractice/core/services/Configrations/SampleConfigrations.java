package com.training.aempractice.core.services.Configrations;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(description = "AEM practise definitions", name="AEM pratice config")
public @interface SampleConfigrations {

  @AttributeDefinition(name="Payment gateway URL",  description = "Payment Config url", type = AttributeType.STRING)
    String paymentUrl() default "https://www.payment.gateway.com";
}
