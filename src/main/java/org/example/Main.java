package org.example;

import org.example.routes.Network;
import org.example.routes.NetworkFactory;

import static org.example.Utility.getProperty;

public class Main {
    public static void main(String[] args) {
        String networkName = getProperty("configured.network");
        NetworkFactory networkFactory = new NetworkFactory();
        Network network = networkFactory.getNetwork(networkName);

        // 1) print data representing all, "subway" routes - using option 2 - API to filter results
        System.out.println("================================================= Question 1 ==");
        network.printRouteLongNames();

        // 2) print the stops related details for routes (route with maximum, minimum stops, connection stops)
        System.out.println("================================================= Question 2 ==");
        network.printRouteStops();

        // 3) print possible routes for traveling between two stops
        System.out.println("================================================= Question 3 ==");
        //String stopOne = "North Station";
        //String stopTwo = "Union Square";
        String stopOne = "Alewife";
        String stopTwo = "Maverick";
        network.printStopsToRoute(stopOne, stopTwo);
    }
}
