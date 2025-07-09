package com.shopify.graphql.resolver;

import com.shopify.graphql.model.Store;
import com.shopify.graphql.service.ShopifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Resolver for top-level GraphQL queries
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class QueryResolver {

    private final ShopifyService shopifyService;

    /**
     * Gets information about a specific store
     *
     * @param name the name of the store
     * @return the store information
     */
    @QueryMapping
    public Store store(@Argument String name) {
        log.debug("Fetching store information for: {}", name);
        return shopifyService.getStore(name);
    }

    /**
     * Gets information about all available stores
     *
     * @return a list of all stores
     */
    @QueryMapping
    public List<Store> allStores() {
        log.debug("Fetching information for all stores");
        return shopifyService.getAllStores();
    }
}
