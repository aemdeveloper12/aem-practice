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

    private static final Logger log =
            LoggerFactory.getLogger(SaveFeedbackServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response) throws IOException {

        log.debug("SaveFeedbackServlet :: POST request received");

        ResourceResolver resourceResolver = null;

        try {
            // 1. Get service resolver
            resourceResolver = CommonUtils.getWriteResolver(resolverFactory);
            log.debug("Service ResourceResolver obtained successfully");

            // 2. Parse request JSON
            JsonObject json =
                    JsonParser.parseReader(request.getReader()).getAsJsonObject();
            log.debug("Request JSON parsed successfully: {}", json);

            String comment = json.get("comment").getAsString();
            String userAgent = request.getHeader("User-Agent");

            log.debug("Extracted comment length: {}", comment.length());
            log.debug("User-Agent: {}", userAgent);

            // 3. Get parent resource
            String parentPath = "/content/aempractice/feedback";
            Resource parent = resourceResolver.getResource(parentPath);

            if (parent == null) {
                log.error("Parent path does not exist: {}", parentPath);
                response.setStatus(500);
                response.getWriter().write("{\"status\":\"Parent path not found\"}");
                return;
            }

            log.debug("Parent resource found at {}", parent.getPath());

            // 4. Prepare properties
            Map<String, Object> props = new HashMap<>();
            props.put("comment", comment);
            props.put("timestamp", Instant.now().toString());
            props.put("userAgent", userAgent);

            // 5. Create feedback node
            String nodeName = "feedback-" + System.currentTimeMillis();
            Resource feedback =
                    resourceResolver.create(parent, nodeName, props);

            log.debug("Feedback node created at {}", feedback.getPath());

            // 6. Commit changes
            resourceResolver.commit();
            log.info("Feedback successfully saved at {}", feedback.getPath());

            // 7. Send success response
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\"}");

        } catch (Exception e) {
            log.error("Error while saving feedback", e);
            response.setStatus(500);
            response.getWriter().write("{\"status\":\"failed\"}");

        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
                log.debug("ResourceResolver closed");
            }
        }
    }
}
