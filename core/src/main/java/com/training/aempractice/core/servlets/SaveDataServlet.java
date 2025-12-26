package com.training.aempractice.core.servlets;

import com.training.aempractice.core.services.ReadURLData;
import com.training.aempractice.core.utils.CommonUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
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
import java.util.Objects;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=aempractice/components/ajaxcallcomponent",
                "sling.servlet.extensions=json"
        }
)
public class SaveDataServlet extends SlingAllMethodsServlet {

    private static final Logger log =
            LoggerFactory.getLogger(SaveDataServlet.class);

    @Reference
    private ReadURLData readURLData;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws IOException {

        log.info("=== SaveDataServlet POST called ===");

        ResourceResolver resourceResolver = null;

        try {
            // 1️⃣ Get WRITE resolver (IMPORTANT)
           // request.getResourceResolver();
            resourceResolver = CommonUtils.getWriteResolver(resolverFactory);

            log.info("Got service resolver with user: {}",
                    resourceResolver.getUserID());

            // 2️⃣ Call service to fetch external data
            String jsonData = readURLData.getJsonDatafromURI();
            log.info("JSON fetched from service");

            // 3️⃣ Get target CONTENT resource
            Resource resource =
                    resourceResolver.getResource("/content/aempractice/data");

            if (resource == null) {
                log.error("Target resource NOT found");
                response.getWriter().write("Target resource not found");
                return;
            }

            // 4️⃣ Update JCR properties
            ModifiableValueMap map =
                    resource.adaptTo(ModifiableValueMap.class);

            if (map == null) {
                log.error("ModifiableValueMap is null");
                response.getWriter().write("Cannot modify resource");
                return;
            }

            map.put("json", jsonData);
            log.info("JSON property updated");

            // 5️⃣ Commit changes
            resourceResolver.commit();
            log.info("Changes committed successfully");

            response.setContentType("text/plain");
            response.getWriter().write("JSON saved successfully");

        } catch (Exception e) {
            log.error("Error while saving JSON", e);
            response.getWriter().write("JSON creation failed: " + e.getMessage());
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
                log.info("ResourceResolver closed");
            }
        }
    }
}
