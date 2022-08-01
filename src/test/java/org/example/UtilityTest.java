package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.example.Utility.getProperty;
import static org.example.Utility.getResponseJSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UtilityTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getProperty() {
        String propertyName = "configured.network";
        String expected = "MBTA";
        String actual = Utility.getProperty(propertyName);
        assertEquals(expected, actual, "Failed to get property");
    }

    @Test
    void getResponseJSON() throws IOException {
        String requestUrl = "https://api-v3.mbta.com/routes?filter[type]=0,1";
        JSONObject jsonObj = Utility.getResponseJSON(requestUrl);
        JSONArray jsonArray = jsonObj.getJSONArray("data");
        assertNotEquals(0, jsonArray.length(), "Failed to get routes");
    }
}