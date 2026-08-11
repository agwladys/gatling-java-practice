package com.practice.simulations;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class JsonPlaceholderSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("https://jsonplaceholder.typicode.com")
        .acceptHeader("application/json");

    // 2. Scenario - describes what one virtual user does
    ScenarioBuilder scenario = scenario("Correlation Practice")

        // Request #1
        // Get a post and extract userId from the JSON response
        .exec(
            http("Get Post 1")
                .get("/posts/1")
                .check(status().is(200))
                .check(
                    jsonPath("$.userId").saveAs("userId")
                )
        )

        // Print the captured value so we can see that
        // Gatling stored it in the Session
        .exec(session -> {
            System.out.println(
                "Captured userId = " + session.getString("userId")
            );

            return session;
        })

        .pause(1)

        // Request #2
        .exec(
            http("Get Post 2")
                .get("/posts/2")
                .check(status().is(200))
        )

        .pause(1)

        // Request #3
        .exec(
            http("Get Users")
                .get("/users")
                .check(status().is(200))
        );


    // 3. Load configuration
    {
        setUp(
            scenario.injectOpen(
                atOnceUsers(1)
            )
        ).protocols(httpProtocol);
    }
}