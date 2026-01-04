package com.training.aempractice.core.servlets;

import com.training.aempractice.core.utils.CommonUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=/bin/storeUserdata",
                "sling.servlet.extensions=json"
        }
)
public class UserDataServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResourceResolver resourceResolver = null;

        try {
            resourceResolver = CommonUtils.getWriteResolver(resolverFactory);

            Resource parent = resourceResolver.getResource("/content/aempractice/userdata");
            if (parent == null) {
                response.setStatus(400);
                response.getWriter().write("{\"error\":\"Parent path not found\"}");
                return;
            }

            String firstName = request.getParameter("firstName");
            String lastName  = request.getParameter("lastName");
            String address   = request.getParameter("address");
            String age       = request.getParameter("age");

            if (firstName == null || firstName.isEmpty()) {
                response.setStatus(400);
                response.getWriter().write("{\"error\":\"firstName is required\"}");
                return;
            }

            Map<String, Object> properties = new HashMap<>();
            properties.put("firstName", firstName);
            properties.put("lastName", lastName);
            properties.put("address", address);
            properties.put("age", age);

            String nodeName = firstName + "_" + System.currentTimeMillis();

            resourceResolver.create(parent, nodeName, properties);
            resourceResolver.commit();

            response.getWriter().write("{\"status\":\"success\",\"message\":\"Data created successfully\"}");

        } catch (Exception e) {
            LOG.error("Error saving user data", e);
            response.setStatus(500);
            response.getWriter().write("{\"status\":\"error\"}");
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }
}
