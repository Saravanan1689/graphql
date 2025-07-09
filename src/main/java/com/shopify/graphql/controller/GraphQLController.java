package com.shopify.graphql.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the GraphQL API
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class GraphQLController {

    /**
     * Health check endpoint
     *
     * @return a health status message
     */
    @GetMapping("/health")
    public String health() {
        return "GraphQL API is up and running!";
    }

    /**
     * API info endpoint
     *
     * @return API information
     */
    @GetMapping("/info")
    public String info() {
        return "Shopify GraphQL API - v1.0.0";
    }
}
