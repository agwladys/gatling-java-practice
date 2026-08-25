package com.practice.requests;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ShipmentRequests {

    // CREATE
    public static final ChainBuilder CREATE_SHIPMENT =
        exec(
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
        );


    // SEARCH
    public static final ChainBuilder SEARCH_SHIPMENT =
        exec(
            http("Search Created Shipment")
                .get("/posts/#{createdShipmentId}")
                .check(status().is(404))
        );


    // RETURN
    public static final ChainBuilder RETURN_SHIPMENT =
        exec(
            http("Return Shipment")
                .delete("/posts/#{createdShipmentId}")
                .check(status().is(200))
        );


    // VERIFY RETURN
    public static final ChainBuilder VERIFY_RETURN =
        exec(
            http("Verify Returned Shipment")
                .get("/posts/#{createdShipmentId}")
                .check(status().is(404))
        );

}