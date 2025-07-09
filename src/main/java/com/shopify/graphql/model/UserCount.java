package com.shopify.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information about user counts for a store
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCount {
    
    /**
     * The total number of users
     */
    private Integer count;
    
    /**
     * When this information was last updated
     */
    private String lastUpdated;
}
