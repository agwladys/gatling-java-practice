package com.practice.simulations;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class JsonPlaceholderSimulation extends Simulation {

    // HTTP configuration
    HttpProtocolBuilder httpProtocol = http
        .baseUrl("https://jsonplaceholder.typicode.com")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");


    // Synthetic shipment test data
    FeederBuilder<String> shipmentFeeder =
        csv("data/shipments.csv").queue();


    // Shipment scenario
    ScenarioBuilder scenario = scenario("Shipment Workflow")

        // Get one shipment from the CSV
        .feed(shipmentFeeder)


        // ---------------------------------------------
        // CREATE SHIPMENT
        // ---------------------------------------------
        .exec(
            http("Create Shipment")
                .post("/posts")
                .body(
                    ElFileBody("bodies/create-shipment.json")
                )
                .check(status().is(201))
                .check(
                    jsonPath("$.id")
                        .saveAs("createdShipmentId")
                )
        )


        // Print the ID while we're learning/debugging
        .exec(session -> {

            System.out.println(
                "Shipment "
                    + session.getString("shipmentRef")
                    + " created with ID = "
                    + session.getString("createdShipmentId")
            );

            return session;
        })


        .pause(1)


        // ---------------------------------------------
        // SEARCH CREATED SHIPMENT
        // ---------------------------------------------
        .exec(
    http("Search Created Shipment")
        .get("/posts/#{createdShipmentId}")
        .check(status().is(404))
)

.pause(1)

.exec(
    http("Return Shipment")
        .delete("/posts/#{createdShipmentId}")
        .check(status().is(200))
)

.pause(1)

.exec(
    http("Verify Returned Shipment")
        .get("/posts/#{createdShipmentId}")
        .check(status().is(404))
);




    // Load configuration
    {
        setUp(
            scenario.injectOpen(
                atOnceUsers(5)
            )
        ).protocols(httpProtocol);
    }
}