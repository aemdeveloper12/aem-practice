package com.training.aempractice.core.servlets;

import com.training.aempractice.core.services.ReadURLData;
import com.training.aempractice.core.utils.CommonUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

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
    private ReadURLData readURLData;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {


        LOG.info("SaveFeedbackServlet :: POST request started");
        long timestamp = System.currentTimeMillis();
        String userAgent= request.getHeader("User-Agent");
        ResourceResolver resourceResolver = null;

        try {
            resourceResolver = CommonUtils.getWriteResolver(resolverFactory);
            LOG.debug("Service ResourceResolver obtained");

            String jsonData = readURLData.getJsonDatafromURI();
            LOG.debug("JSON data received from service: {}", jsonData);

            Resource resource = resourceResolver.getResource("/content/aempractice/feedbackdata");

            if (resource == null) {
                LOG.error("Target resource not found");
                response.sendError(SlingHttpServletResponse.SC_NOT_FOUND, "Target resource not found");
                return;
            }

            ModifiableValueMap valueMap = resource.adaptTo(ModifiableValueMap.class);
            if (valueMap == null) {
                LOG.error("Failed to adapt resource to ModifiableValueMap");
                response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update node");
                return;
            }

            valueMap.put("jsonMessage", jsonData);
            valueMap.put("Timestamp", timestamp);
            valueMap.put("UserAgent", userAgent);
            resourceResolver.commit();

            LOG.info("JSON data saved successfully");

            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\"}");

        } catch (Exception e) {
            LOG.error("Error while saving JSON data", e);
            response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JSON creation failed");
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
                LOG.debug("ResourceResolver closed");
            }
        }
    }
}
