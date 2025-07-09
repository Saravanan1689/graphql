package com.shopify.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information about revenue for a store
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Revenue {
    
    /**
     * The total revenue amount
     */
    private Float total;
    
    /**
     * The currency of the revenue
     */
    private String currency;
    
    /**
     * When this information was last updated
     */
    private String lastUpdated;
}
