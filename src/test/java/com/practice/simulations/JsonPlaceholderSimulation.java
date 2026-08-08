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

    ScenarioBuilder scenario = scenario("Browse Posts")

    .exec(
        http("Get Post 1")
            .get("/posts/1")
            .check(status().is(200))
    )

    .pause(1)

    .exec(
        http("Get Post 2")
            .get("/posts/2")
            .check(status().is(200))
    )

    .pause(1)

    .exec(
        http("Get Users")
            .get("/users")
            .check(status().is(200))
    );

    {
        setUp(
            scenario.injectOpen(
                rampUsers(5).during(10)
            )
        ).protocols(httpProtocol);
    }
}