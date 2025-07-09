package com.shopify.graphql.service;

import com.shopify.graphql.model.CartAbandonment;
import com.shopify.graphql.model.OrderCount;
import com.shopify.graphql.model.Revenue;
import com.shopify.graphql.model.Store;
import com.shopify.graphql.model.UserCount;

import java.util.List;

/**
 * Service interface for interacting with the Shopify API
 */
public interface ShopifyService {

    /**
     * Gets information about a specific store
     *
     * @param storeName the name of the store
     * @return the store information
     */
    Store getStore(String storeName);

    /**
     * Gets information about all available stores
     *
     * @return a list of all stores
     */
    List<Store> getAllStores();

    /**
     * Gets user count information for a store
     *
     * @param storeName the name of the store
     * @return the user count information
     */
    UserCount getUserCount(String storeName);

    /**
     * Gets order count information for a store
     *
     * @param storeName the name of the store
     * @return the order count information
     */
    OrderCount getOrderCount(String storeName);

    /**
     * Gets cart abandonment information for a store
     *
     * @param storeName the name of the store
     * @return the cart abandonment information
     */
    CartAbandonment getCartAbandonment(String storeName);

    /**
     * Gets revenue information for a store
     *
     * @param storeName the name of the store
     * @return the revenue information
     */
    Revenue getRevenue(String storeName);
}
