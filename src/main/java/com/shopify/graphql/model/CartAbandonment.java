package com.shopify.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information about cart abandonment for a store
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartAbandonment {
    
    /**
     * The number of abandoned carts
     */
    private Integer count;
    
    /**
     * When this information was last updated
     */
    private String lastUpdated;
}
