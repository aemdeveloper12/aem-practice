package com.training.aempractice.core.servlets;

import com.training.aempractice.core.services.GreetingService;
import com.training.aempractice.core.services.MessageService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;


@Component(service = Servlet.class,
property = {
         "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/greeting"
}
)
public class GreetingServlet  extends SlingSafeMethodsServlet {

    private final Logger log = LoggerFactory.getLogger(getClass());

@Reference
  private GreetingService greetingService;

    @Reference
    private MessageService messageService;

protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {


    String name = request.getParameter("name");

    if (name == null) {
       name="Student";
    }

    log.info("Request received with name: {}", name);
    String greeting = greetingService.getGreeting(name);
    String message = messageService.getMessage(name);

    log.info("GreetingService response: {}", greeting);
    log.info("MessageService response: {}", message);

    JSONObject json = new JSONObject();
    try {
        json.put("Greeting", greeting);
        json.put("message", message);
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }

    response.setContentType("application/json");
    response.getWriter().write(json.toString());

}

}
