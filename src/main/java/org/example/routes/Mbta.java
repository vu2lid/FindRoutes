package org.example.routes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.Utility.getProperty;
import static org.example.Utility.getResponseJSON;

public class Mbta implements Network {

    public static final String MBTA_API_SEARCH_PATH_ROUTES = "mbta.api.search.path.routes";
    public static final String MBTA_API_SEARCH_PATH_ROUTE_PATTERNS_STOPS = "mbta.api.search.path.route_patterns.stops";

    private TreeMap<String, String> routesMap = null;
    private TreeMap<String, ArrayList<String>> stopNameToStopIds = null;
    private TreeMap<String, TreeMap<String, ArrayList<String>>> connectionStops = null;
    public Mbta() {
    }

    /**
     * Synthesizes the enpoint URL based on the provided string path
     *
     * @param apiPath
     * @return synthesized url to the endpoint
     */
    @Override
    public String getApiUrl(String apiPath) {
        String apiKey = getProperty("mbta.api.key");
        String apiBaseUrl = getProperty("mbta.api.base.url");
        String apiUrl = String.format(apiKey.equals("") ? "%s%s" : "%s%s&api_key=%s", apiBaseUrl, apiPath, apiKey);
        return apiUrl;
    }

    /**
     * Prints a list of long names of all the routes
     */
    @Override
    public void printRouteLongNames() {
        try {
            if(routesMap == null) routesMap = collectRouteLongNames(getApiUrl(getProperty(MBTA_API_SEARCH_PATH_ROUTES)));
            System.out.println(routesMap.values().stream().collect(Collectors.joining(", ")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a collection of routeIds
     *
     * @return Collection of routeIds
     */
    @Override
    public Collection<String> getRouteIds() {
        Collection<String> routeIds = null;
        try {
            if(routesMap == null) routesMap = collectRouteLongNames(getApiUrl(getProperty(MBTA_API_SEARCH_PATH_ROUTES)));
            routeIds = routesMap.keySet();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return routeIds;
    }

    /**
     * Prints route stops
     */
    @Override
    public void printRouteStops() {
        connectionStops = new TreeMap<>();
        int maxStops = 0;
        String maxStopsRouteId = null;
        int minStops = 0;
        String minStopsRouteId = null;
        var routeIds = getRouteIds();
        for (String routeId : routeIds) {
            String requestUrl = getApiUrl(String.format(getProperty(MBTA_API_SEARCH_PATH_ROUTE_PATTERNS_STOPS), routeId));
            try {
                var stopsMap = collectRouteStops(requestUrl);
                if (minStopsRouteId == null && maxStopsRouteId == null) {
                    minStops = stopsMap.get(routeId).values().size();
                    minStopsRouteId = routeId;
                    maxStops = stopsMap.get(routeId).values().size();
                    maxStopsRouteId = routeId;
                }

                if (stopsMap.get(routeId).values().size() <= minStops) {
                    minStops = stopsMap.get(routeId).values().size();
                    minStopsRouteId = routeId;
                }
                if (stopsMap.get(routeId).values().size() >= maxStops) {
                    maxStops = stopsMap.get(routeId).values().size();
                    maxStopsRouteId = routeId;
                }
                //find stops with connection between different routes
                for (String stopId : stopsMap.get(routeId).keySet()) {
                    if (connectionStops.get(stopId) == null) {
                        ArrayList<String> routeNames = new ArrayList<>();
                        TreeMap<String, ArrayList<String>> routesOfStop = new TreeMap<>();
                        routesOfStop.put(stopsMap.get(routeId).get(stopId), routeNames);
                        connectionStops.put(stopId, routesOfStop);
                    }
                    var stopName = stopsMap.get(routeId).get(stopId);
                    connectionStops.get(stopId).get(stopName).add(routeId);
                }
                //System.out.println(connectionStops);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Route with maximum number of " + maxStops + " stops is " + maxStopsRouteId);
        System.out.println("Route with minimum number of " + minStops + " stops is " + minStopsRouteId);
        for (String stopId : connectionStops.keySet()) {
            for (String stopName : connectionStops.get(stopId).keySet()) {
                if (connectionStops.get(stopId).get(stopName).size() > 1) {
                    var connectingRoutes = connectionStops.get(stopId).get(stopName).stream().collect(Collectors.joining(", "));
                    System.out.println("The stop with the stop name'" + stopName + "' and stopId " + stopId + " connects the following routes: " + connectingRoutes);
                }
            }
        }
    }

    /**
     * Given two stops prints possible routes connecting those two stops
     */
    @Override
    public void printStopsToRoute(String stopOne, String stopTwo) {
        // find the stopIds corresponding to stopOne and stopTwo stopNames - from stopNameToStopIds
        // find the route names of these stops - from connectionStops
        // if these route names intersect - if these are route names common to both - list those route names
        if(stopNameToStopIds.containsKey(stopOne) && stopNameToStopIds.containsKey(stopTwo)) {
            var stopIdsStopOne = stopNameToStopIds.get(stopOne);
            var stopIdsStopTwo = stopNameToStopIds.get(stopTwo);
            // see if there are possible connections through connecting stops in routes
            // if the two lists have common elements there is a route between the two stops
            Set<String> stopOneRoutesSet = new LinkedHashSet<>();
            for(String stopId: stopIdsStopOne) {
                stopOneRoutesSet.addAll(connectionStops.get(stopId).get(stopOne));
            }
            Set<String> stopTwoRoutesSet = new LinkedHashSet<>();
            for(String stopId: stopIdsStopTwo) {
                stopTwoRoutesSet.addAll(connectionStops.get(stopId).get(stopTwo));
            }
            Set<String> combinedSet = new LinkedHashSet<>(stopOneRoutesSet);
            combinedSet.addAll(stopTwoRoutesSet);
            if((stopOneRoutesSet.size() + stopTwoRoutesSet.size()) > combinedSet.size()) {
                System.out.println("Possible routes connecting stops '"+stopOne+"' and '"+stopTwo+"' are "+combinedSet);
            }else {
                System.out.println("There are no routes connecting stops '"+stopOne+"' and '"+stopTwo);
            }
        }else{
            System.out.println("Unable to find the stops "+stopOne+" and "+stopTwo);
        }
    }

    private TreeMap<String, TreeMap<String, String>> collectRouteStops(String requestUrl) throws IOException {
        JSONObject jsonObj = getResponseJSON(requestUrl);
        TreeMap<String, TreeMap<String, String>> stopsMap = null;
        if (jsonObj != null) {
            stopsMap = new TreeMap<>();
            String routeId = jsonObj.getJSONObject("data").getString("id");
            if (stopsMap.get(routeId) == null) stopsMap.put(routeId, new TreeMap<>());
            JSONArray jsonArray = jsonObj.getJSONArray("included");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if (json.getString("type").equalsIgnoreCase("stop")) {
                    JSONObject attributes = json.getJSONObject("attributes");
                    String name = attributes.getString("name");
                    String id = json.getString("id");
                    stopsMap.get(routeId).put(id, name);
                    // collect a map of stop names to possible stopIds
                    if(stopNameToStopIds == null) stopNameToStopIds = new TreeMap<>();
                    if(stopNameToStopIds.get(name) == null) stopNameToStopIds.put(name, new ArrayList<>());
                    stopNameToStopIds.get(name).add(id);
                }
            }
            //System.out.println(stopsMap);
        }
        return stopsMap;
    }

    private TreeMap<String, String> collectRouteLongNames(String requestUrl) throws IOException {
        TreeMap<String, String> routeIdToLongNameMap = null;
        JSONObject jsonObj = getResponseJSON(requestUrl);
        if (jsonObj != null) {
            routeIdToLongNameMap = new TreeMap<>();
            JSONArray searchResult = new JSONArray();
            jsonObj.toJSONArray(searchResult);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                JSONObject attributes = json.getJSONObject("attributes");
                String long_name = attributes.getString("long_name");
                String id = json.getString("id");
                routeIdToLongNameMap.put(id, long_name);
            }
        }
        return routeIdToLongNameMap;
    }
}
