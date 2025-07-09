package com.shopify.graphql.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for Shopify
 */
@Component
@ConfigurationProperties(prefix = "shopify")
@Data
public class ShopifyProperties {

    private Map<String, StoreConfig> stores = new HashMap<>();
    private ClientConfig client = new ClientConfig();

    /**
     * Configuration for a single Shopify store
     */
    @Data
    public static class StoreConfig {
        private String accessToken;
        private String usersVisitedUrl;
        private String totalOrdersUrl;
        private String cartAbandonmentUrl;
        private String totalRevenueUrl;
    }

    /**
     * Configuration for the WebClient
     */
    @Data
    public static class ClientConfig {
        private int connectTimeout = 5000;
        private int readTimeout = 5000;
        private int writeTimeout = 5000;
    }
}
