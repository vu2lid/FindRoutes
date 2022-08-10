package org.example.routes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RouteGraph {

    private final HashMap<String, ArrayList<String>> routeConnectionList = new HashMap<>();

    /**
     * Adds a new route to the route Graph.
     *
     * @param routeId
     * @return was it successful
     */
    public boolean addRoute(String routeId) {
        if (routeConnectionList.get(routeId) == null) {
            routeConnectionList.put(routeId, new ArrayList<String>());
            return true;
        }
        return false;
    }

    /**
     * Adds a route connection to the route Graph.
     *
     * @param routeId1
     * @param routeId2
     * @return successful or not
     */
    public boolean addConnection(String routeId1, String routeId2) {
        if (routeConnectionList.get(routeId1) != null && routeConnectionList.get(routeId2) != null) {
            routeConnectionList.get(routeId1).add(routeId2);
            routeConnectionList.get(routeId2).add(routeId1);
            return true;
        }
        return false;
    }

    /**
     * Traverses the route Graph recursively to find a route (some route - may not be the best).
     *
     * @param startRoute
     * @param endRoute
     * @param routeSet
     */
    public void findRoute(String startRoute, String endRoute, Set<String> routeSet) {
        // System.out.println("Start route: "+startRoute);
        routeSet.add(startRoute);
        for (String route : routeConnectionList.get(startRoute)) {
            if (!routeSet.contains(route) && !routeSet.contains(endRoute)) {
                findRoute(route, endRoute, routeSet);
            }
        }
    }
}
