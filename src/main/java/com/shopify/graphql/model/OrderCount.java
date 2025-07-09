package com.shopify.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information about order counts for a store
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCount {
    
    /**
     * The total number of orders
     */
    private Integer count;
    
    /**
     * When this information was last updated
     */
    private String lastUpdated;
}
