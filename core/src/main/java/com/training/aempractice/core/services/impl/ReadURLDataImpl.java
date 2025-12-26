package com.training.aempractice.core.services.impl;

import com.training.aempractice.core.services.ExternalAPIConfigsService;
import com.training.aempractice.core.services.JsonConfigs;
import com.training.aempractice.core.services.ReadURLData;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Component(service =ReadURLData.class, immediate = true)
public class ReadURLDataImpl implements ReadURLData {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference
    JsonConfigs jsonConfigs;

    @Reference
    ExternalAPIConfigsService externalAPIConfigsService;

    @Override
    public String getJsonDatafromURI() throws IOException {

        String apiurl= externalAPIConfigsService.externalUrl();

        URL url =new URL(apiurl);
        //open connection
       HttpURLConnection  con =(HttpURLConnection) url.openConnection();

       //get the data
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        log.info("HTTP connection created, sending request");

        int responseCode = con.getResponseCode();
        log.info("Received HTTP response code: {}", responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            log.error("API call failed with response code {}", responseCode);
            return "Error: API returned response code " + responseCode;
        }
        // Read response
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );

        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        reader.close();
        con.disconnect();

        log.debug("API response received successfully");
        log.info("Returning API response to caller");
        return result.toString();


    }

    @Activate
    public void Activated()
    {
        log.info("Service is Activated");
    }

    @Deactivate
    public void DeActivated()
    {
        log.info("service is deactivated");
    }
}
