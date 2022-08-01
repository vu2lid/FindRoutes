package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void getResponseJSON() {
    }
}