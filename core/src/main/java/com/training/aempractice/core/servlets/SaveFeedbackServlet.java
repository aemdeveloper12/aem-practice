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
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=aempractice/components/feedback",
                "sling.servlet.extensions=json"
        }
)
public class SaveFeedbackServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(SaveFeedbackServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        LOG.info("SaveFeedbackServlet :: POST request started");

        response.setContentType("application/json");
        ResourceResolver resourceResolver = null;

        try {
            // 1. Parse JSON body
            JsonObject json =
                    JsonParser.parseReader(request.getReader()).getAsJsonObject();

            if (!json.has("comment")) {
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"comment is required\"}");
                return;
            }

            String comment = json.get("comment").getAsString();
            String userAgent = request.getHeader("User-Agent");

            // 2. Get service resolver (write access)
            resourceResolver = CommonUtils.getWriteResolver(resolverFactory);
            LOG.debug("Service ResourceResolver obtained");

            // 3. Get parent resource
            Resource parent =
                    resourceResolver.getResource("/content/aempractice/feedbackdata");

            if (parent == null) {
                LOG.error("Target resource not found");
                response.sendError(
                        SlingHttpServletResponse.SC_NOT_FOUND,
                        "Target resource not found"
                );
                return;
            }

            // 4. Prepare properties
            Map<String, Object> properties = new HashMap<>();
            properties.put("comment", comment);
            properties.put("timestamp", Instant.now().toString());
            properties.put("userAgent", userAgent);

            // 5. Create resource
            resourceResolver.create(
                    parent,
                    "feedback-" + System.currentTimeMillis(),
                    properties
            );

            // 6. Commit changes
            resourceResolver.commit();
            LOG.info("JSON data saved successfully");

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
