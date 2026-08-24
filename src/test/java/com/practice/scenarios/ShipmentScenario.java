package com.practice.scenarios;

import com.practice.requests.ShipmentRequests;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ShipmentScenario {

    // Shipment test data
    private static final FeederBuilder<String> shipmentFeeder =
        csv("data/shipments.csv").circular();


    // Complete shipment business workflow
    public static final ScenarioBuilder SHIPMENT_SCENARIO =
        scenario("Shipment Workflow")

            .feed(shipmentFeeder)

            .exec(ShipmentRequests.CREATE_SHIPMENT)

            .pause(1)

            .exec(ShipmentRequests.SEARCH_SHIPMENT)

            .pause(1)

            .exec(ShipmentRequests.RETURN_SHIPMENT)

            .pause(1)

            .exec(ShipmentRequests.VERIFY_RETURN);
}