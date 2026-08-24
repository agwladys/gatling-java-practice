package com.practice.simulations;

import com.practice.config.HttpConfig;
import com.practice.scenarios.ShipmentScenario;

import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;

public class JsonPlaceholderSimulation extends Simulation {

    {
        setUp(
            ShipmentScenario.SHIPMENT_SCENARIO.injectOpen(
                rampUsers(20).during(30)
            )
        )
        .protocols(HttpConfig.HTTP_PROTOCOL)
        .assertions(
            global().failedRequests().percent().lt(1.0),
            global().responseTime().percentile(95).lt(500)
        );
    }
}