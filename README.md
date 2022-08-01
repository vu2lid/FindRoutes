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
reusable code, possibility to expand refactor easily, ease of configuring various network dependent parameters, some tests.

## Design notes
### Question 1
notes for 1
### Question 2
notes for 2
### Question 3
notes for 3

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
