package com.practice.simulations;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.core.FeederBuilder;


import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class JsonPlaceholderSimulation extends Simulation {

    // ---------------------------------------------------------
    // 1. HTTP Configuration
    // ---------------------------------------------------------

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("https://jsonplaceholder.typicode.com")
        .acceptHeader("application/json");


    // ---------------------------------------------------------
    // 2. Shipment CSV Feeder
    // ---------------------------------------------------------
    // Gatling looks for this file under:
    // src/test/resources/data/shipments.csv
    //
    // queue() means:
    // first user gets first row,
    // second user gets second row, etc.
    // ---------------------------------------------------------

    FeederBuilder<String> shipmentFeeder =
    csv("data/shipments.csv").queue();
    // ---------------------------------------------------------
    // 3. Scenario
    // ---------------------------------------------------------

    ScenarioBuilder scenario = scenario("Shipment Feeder Practice")

        // Take one row from shipments.csv
        // and put its values into the Gatling Session.
        .feed(shipmentFeeder)


    .exec(
        http("Create Shipment")
            .post("/posts")
            .header("Content-Type", "application/json")
            .body(
                ElFileBody("bodies/create-shipment.json")
            )
            .check(status().is(201))
    )

    .pause(1)





        // Print shipment information from the Session.
        // This is just for learning/debugging.
        .exec(session -> {

            System.out.println("----- Shipment Data -----");

            System.out.println(
                "Reference: "
                    + session.getString("shipmentRef")
            );

            System.out.println(
                "Origin ZIP: "
                    + session.getString("originZip")
            );

            System.out.println(
                "Destination ZIP: "
                    + session.getString("destinationZip")
            );

            System.out.println(
                "Weight: "
                    + session.getString("weight")
            );

            System.out.println(
                "Service: "
                    + session.getString("serviceType")
            );

            return session;
        })

        .pause(1)


        // -----------------------------------------------------
        // Request #1
        //
        // Get Post 1.
        //
        // We also extract userId from the JSON response
        // and save it into the Gatling Session.
        // -----------------------------------------------------

        .exec(
            http("Get Post 1")
                .get("/posts/1")
                .check(status().is(200))
                .check(
                    jsonPath("$.userId").saveAs("userId")
                )
        )


        // -----------------------------------------------------
        // Print the correlated userId.
        //
        // This proves that userId was successfully extracted
        // from the HTTP response and stored in the Session.
        // -----------------------------------------------------

        .exec(session -> {

            System.out.println(
                "Captured userId = "
                    + session.getString("userId")
            );

            return session;
        })

        .pause(1)


        // -----------------------------------------------------
        // Request #2
        // -----------------------------------------------------

        .exec(
            http("Get Post 2")
                .get("/posts/2")
                .check(status().is(200))
        )

        .pause(1)


        // -----------------------------------------------------
        // Request #3
        //
        // Use the userId captured from Request #1.
        //
        // If userId = 1:
        //
        // /users/#{userId}
        //
        // becomes:
        //
        // /users/1
        // -----------------------------------------------------

        .exec(
            http("Get User By Captured ID")
                .get("/users/#{userId}")
                .check(status().is(200))
        );


    // ---------------------------------------------------------
    // 4. Load Configuration
    // ---------------------------------------------------------
    // We're deliberately using only one virtual user while
    // learning how feeders and Sessions work.
    // ---------------------------------------------------------

    {
        setUp(
            scenario.injectOpen(
                atOnceUsers(5)
            )
        ).protocols(httpProtocol);
    }
}