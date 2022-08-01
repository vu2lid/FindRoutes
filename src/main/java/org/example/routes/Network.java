package org.example.routes;

import java.util.Collection;

public interface Network {
    String getApiUrl(String path);

    void printRouteLongNames();
    Collection<String> getRouteIds();
    void printRouteStops();
    void printStopsToRoute(String beginStop, String endStop);
}
