package com.shopify.graphql.resolver;

import com.shopify.graphql.model.CartAbandonment;
import com.shopify.graphql.model.OrderCount;
import com.shopify.graphql.model.Revenue;
import com.shopify.graphql.model.Store;
import com.shopify.graphql.model.UserCount;
import com.shopify.graphql.service.ShopifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

/**
 * Resolver for Store type fields
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class StoreResolver {

    private final ShopifyService shopifyService;

    /**
     * Gets user count information for a store
     *
     * @param store the store
     * @return the user count information
     */
    @SchemaMapping(typeName = "Store", field = "userCount")
    public UserCount getUserCount(Store store) {
        log.debug("Fetching user count for store: {}", store.getName());
        return shopifyService.getUserCount(store.getName());
    }

    /**
     * Gets order count information for a store
     *
     * @param store the store
     * @return the order count information
     */
    @SchemaMapping(typeName = "Store", field = "orderCount")
    public OrderCount getOrderCount(Store store) {
        log.debug("Fetching order count for store: {}", store.getName());
        return shopifyService.getOrderCount(store.getName());
    }

    /**
     * Gets cart abandonment information for a store
     *
     * @param store the store
     * @return the cart abandonment information
     */
    @SchemaMapping(typeName = "Store", field = "cartAbandonment")
    public CartAbandonment getCartAbandonment(Store store) {
        log.debug("Fetching cart abandonment for store: {}", store.getName());
        return shopifyService.getCartAbandonment(store.getName());
    }

    /**
     * Gets revenue information for a store
     *
     * @param store the store
     * @return the revenue information
     */
    @SchemaMapping(typeName = "Store", field = "revenue")
    public Revenue getRevenue(Store store) {
        log.debug("Fetching revenue for store: {}", store.getName());
        return shopifyService.getRevenue(store.getName());
    }
}
