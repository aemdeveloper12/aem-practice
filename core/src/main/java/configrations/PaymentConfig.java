package configrations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(description = "Configs  for payments ", name = "Payment Configs")
public @interface  PaymentConfig {

    @AttributeDefinition(name="URL for payment", description = "Payment URL", type = AttributeType.STRING)
    String paymentUrl() default  "https://www.payments.com";
}
