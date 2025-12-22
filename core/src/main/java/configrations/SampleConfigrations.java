package configrations;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(description = "AEM practise definitions", name="AEM pratice config")
public @interface SampleConfigrations {

  @AttributeDefinition(name="json gateway URL",  description = "json Config url", type = AttributeType.STRING)
    String jsonUrl() default "https://dummyjson.com/todos/1";


}
