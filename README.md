# FindRoutes
Analyze and find routing related answers related to a transportation network

This tries to answer the following three questions (Please see 'Running' below. Check console for output.):
1. print data representing all, "subway" routes - using option 2 - API to filter results
2. print the stops related details for routes (route with maximum, minimum stops, connection stops)
3. print possible routes for traveling between two stops

Please check the references and MBTA API Swagger documentation at:
```
https://api-v3.mbta.com/docs/swagger/index.html
```

## Goal

The overall goal was to find answers to the above questions within the available time. Additional considerations were
reusable code, possibility to expand and refactor easily, ease of configuring various network dependent parameters, adding more networks
easily, some tests.

## Design notes
### Question 1
Chose to rely on the API to filter the results since API will have access to the up-to-date backend data. This will also reduce 
use of other system resources, since we will be downloading and process the minimum required data for the purpose. ``routeMap``
saves the mapping between route id and long name of the route.
### Question 2
*Caution: a specific API endpoint (mbta.api.search.path.route_patterns.stops in the properties file) using route_patterns was used 
to collect stops connected to a specific route (based on documentation and search). This may not be the correct approach.* 
``connectionStops`` will have the collected stops and route names data.
### Question 3
All possible routes between two stops are found using saved ``stopNameToStopIds`` and ``connectionStops``.
### General notes
Apache ``httpclient`` library was selected, since it seems to be widely used and documented and open source. 
Due to similar reasons ``json`` library was used. It was decided to use extract the required data by traversing
the returned JSON structure (since it was faster to implement). This code which is network dependant 
(in this case dependent on MBTA response JSON).
is included in two private functions.
### Things that can be changed
Add class models corresponding to the JSON schema used. Add more unit and integration tests. Refactor the code to
extract more code which is not dependent on a specific network (most of the public methods are not network specific).
Refactor code to avoid/reduce API requests and cache data locally (it looks like it is possible to request only the
new/changed data).
## Getting Started

The following instructions will get you a copy of the project up and running on your local machine.

### Environment

The project was tested/developed using:
```
Java 17, 
org.apache.httpcomponents.httpclient library, 
org.json.json library and others. Please check pom.xml. 
``` 
### Installing

Clone and import/run this as a Maven project.
```
git clone https://github.com/vu2lid/FindRoutes.git
```

## Running

Run the app by:
```
Run main.java after building and all the Maven dependencies are satisfied. 
Check comments in main.java. Check console for output. 
Changing start and end stops for Question 3 can be done in main.java. 
If there is an API key, configure it in applications.properties. 
Various endpoints accessed are specified in application.properties. 
```

## References

* [MBTA API](https://github.com/mbta/api) - MBTA API V3 (as of 20220731)
* [MBTA Developers page](https://www.mbta.com/developers) - Various MBTA developer info
* [MBTA V3 API Swagger documentation](https://api-v3.mbta.com/docs/swagger/index.html) - Swagger testing and documentation for MBTA V3 API

## License

This project is licensed under the GPLv3 License
