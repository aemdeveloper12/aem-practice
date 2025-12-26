package configrations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(description = "Configuration for external API endpoint", name="External API ConfigurationConfiguration for external API endpoint")
public @interface  ExternalApiConfig {

    @AttributeDefinition(description = "Base URL of the external API", name = "External API URL",type= AttributeType.STRING)
    String externalApiUrl() default "https://dummyjson.com/todos/1" ;



}
