package com.practice.config;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class HttpConfig {

    public static final String BASE_URL =
        System.getProperty(
            "baseUrl",
            "https://jsonplaceholder.typicode.com"
        );

    public static final HttpProtocolBuilder HTTP_PROTOCOL = http
        .baseUrl(BASE_URL)
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");
}