package org.example.routes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class RouteGraphTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addRoute() {
    }

    @Test
    void addConnection() {
    }

    @Test
    void findRoute() {
        RouteGraph myRouteGraph = new RouteGraph();

        myRouteGraph.addRoute("22");
        myRouteGraph.addRoute("98");
        myRouteGraph.addRoute("12");
        myRouteGraph.addRoute("41");
        myRouteGraph.addRoute("25");
        myRouteGraph.addConnection("22", "76");
        myRouteGraph.addConnection("22", "44");
        myRouteGraph.addConnection("98", "12");
        myRouteGraph.addConnection("98", "41");
        myRouteGraph.addConnection("12", "41");
        myRouteGraph.addConnection("25", "41");
        myRouteGraph.addConnection("25", "76");

        Set<String> routeSet = new TreeSet<>();
        myRouteGraph.findRoute("25", "98", routeSet);
        String expected = "[25, 41, 98]";
        String actual = routeSet.toString();

        assertEquals(expected, actual, "Failed to find route");
    }
}