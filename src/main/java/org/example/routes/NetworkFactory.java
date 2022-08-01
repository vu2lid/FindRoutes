package org.example.routes;

public class NetworkFactory {
    public Network getNetwork(String networkType) {
        if (networkType == null) {
            return null;
        } else if (networkType.equalsIgnoreCase("MBTA")) {
            return new Mbta();
        }
        return null;
    }
}
