package com.training.aempractice.core.servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
                "sling.servlet.resourceTypes=aempractice/components/feedback-resource",
                "sling.servlet.extensions=json"
        }
)
public class UserDataResourceServlet extends SlingAllMethodsServlet {

    private static final Logger LOG =
            LoggerFactory.getLogger(UserDataResourceServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResourceResolver resourceResolver = null;

        try {
            // 1. Parse JSON body
            JsonObject json =
                    JsonParser.parseReader(request.getReader()).getAsJsonObject();

            // 2. Validate required fields
            if (!(json.has("firstName")
                    && json.has("lastName")
                    && json.has("age")
                    && json.has("address"))) {

                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                response.getWriter()
                        .write("{\"error\":\"firstName, lastName, age and address are required\"}");
                return;
            }

            String firstName = json.get("firstName").getAsString();
            String lastName  = json.get("lastName").getAsString();
            String age       = json.get("age").getAsString();
            String address   = json.get("address").getAsString();

            // 3. Get service ResourceResolver
            resourceResolver = CommonUtils.getWriteResolver(resolverFactory);

            // 4. Get parent resource
            Resource parent =
                    resourceResolver.getResource("/content/aempractice/userdata-resourcebased");

            if (parent == null) {
                LOG.error("Target resource not found");
                response.sendError(
                        SlingHttpServletResponse.SC_NOT_FOUND,
                        "Target resource not found"
                );
                return;
            }

            // 5. Prepare properties
            Map<String, Object> properties = new HashMap<>();
            properties.put("firstName", firstName);
            properties.put("lastName", lastName);
            properties.put("age", age);
            properties.put("address", address);

            // 6. Create child resource
            resourceResolver.create(
                    parent,
                    "userdata-" + System.currentTimeMillis(),
                    properties
            );

            // 7. Commit
            resourceResolver.commit();

            LOG.info("User data saved successfully");
            response.getWriter().write("{\"status\":\"success\"}");

        } catch (Exception e) {
            LOG.error("Error while saving JSON data", e);
            response.sendError(
                    SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "JSON save failed"
            );
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
                LOG.debug("ResourceResolver closed");
            }
        }
    }
}
