package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utility {
    public static String configurationProperties = "application.properties";

    /**
     * Returns the configured property value of the provided key
     *
     * @param propertyName
     * @return value corresponding to the provided key
     */
    public static String getProperty(String propertyName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(configurationProperties)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(propertyName);
    }

    /**
     * Returns the JSON response from the request
     *
     * @param requestUrl
     * @return JSON response from the request or null
     * @throws IOException
     */
    public static JSONObject getResponseJSON(String requestUrl) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(requestUrl);
        CloseableHttpResponse response = httpClient.execute(request);
        JSONObject jsonObj = null;
        try {
            //System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                jsonObj = new JSONObject(EntityUtils.toString(entity));
            }
        } finally {
            response.close();
            httpClient.close();
        }
        return jsonObj;
    }
}
