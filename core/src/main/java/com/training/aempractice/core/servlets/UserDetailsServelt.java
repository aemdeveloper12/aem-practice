package com.training.aempractice.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class,
        property ={"sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/userdetails"})
public class UserDetailsServelt extends SlingSafeMethodsServlet {

    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws IOException {


        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"Hello from servlet\"}");

    }
}




