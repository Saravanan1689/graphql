package com.shopify.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Shopify store with various metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    
    /**
     * The name of the store
     */
    private String name;
    
    /**
     * User count information for the store
     */
    private UserCount userCount;
    
    /**
     * Order count information for the store
     */
    private OrderCount orderCount;
    
    /**
     * Cart abandonment information for the store
     */
    private CartAbandonment cartAbandonment;
    
    /**
     * Revenue information for the store
     */
    private Revenue revenue;
}
