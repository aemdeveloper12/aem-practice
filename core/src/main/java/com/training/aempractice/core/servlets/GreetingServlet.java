package com.training.aempractice.core.servlets;

import com.training.aempractice.core.services.GreetingService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.servlet.Servlet;
import java.io.IOException;


@Component(service = Servlet.class,
property = {
         "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/greeting"
}
)
public class GreetingServlet  extends SlingSafeMethodsServlet {

@Reference
  private GreetingService greetingService;

protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {


    String name = request.getParameter("name");

    if (name == null) {
       name="Student";
    }
    String  message = greetingService.getGreeting(name);

    JSONObject json = new JSONObject();
    try {
        json.put("Greeting", message);
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }

    response.setContentType("application/json");
    response.getWriter().write(json.toString());

}

}
