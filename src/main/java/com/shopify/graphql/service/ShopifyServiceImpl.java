package com.shopify.graphql.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.graphql.config.ShopifyProperties;
import com.shopify.graphql.config.ShopifyProperties.StoreConfig;
import com.shopify.graphql.exception.ShopifyApiException;
import com.shopify.graphql.model.CartAbandonment;
import com.shopify.graphql.model.OrderCount;
import com.shopify.graphql.model.Revenue;
import com.shopify.graphql.model.Store;
import com.shopify.graphql.model.UserCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the ShopifyService interface
 */
@Service
@Slf4j
public class ShopifyServiceImpl implements ShopifyService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ShopifyProperties shopifyProperties;

    /**
     * Constructor with debug logging for store properties
     */
    public ShopifyServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper, ShopifyProperties shopifyProperties) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.shopifyProperties = shopifyProperties;
        
        // Debug logging for store properties
        log.debug("Initialized ShopifyServiceImpl with store properties:");
        if (shopifyProperties != null && shopifyProperties.getStores() != null) {
            log.debug("Number of stores configured: {}", shopifyProperties.getStores().size());
            shopifyProperties.getStores().forEach((name, config) -> {
                log.debug("Store: {}", name);
                log.debug("  Access Token: {}", config.getAccessToken() != null ? "Present" : "Missing");
                log.debug("  Users Visited URL: {}", config.getUsersVisitedUrl());
                log.debug("  Total Orders URL: {}", config.getTotalOrdersUrl());
                log.debug("  Cart Abandonment URL: {}", config.getCartAbandonmentUrl());
                log.debug("  Total Revenue URL: {}", config.getTotalRevenueUrl());
            });
        } else {
            log.debug("No stores configured or store properties is null");
        }
    }

    /**
     * Gets information about a specific store
     *
     * @param storeName the name of the store
     * @return the store information
     */
    @Override
    public Store getStore(String storeName) {
        StoreConfig storeConfig = getStoreConfig(storeName);
        
        return Store.builder()
                .name(storeName)
                .build();
    }

    /**
     * Gets information about all available stores
     *
     * @return a list of all stores
     */
    @Override
    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        
        for (Map.Entry<String, StoreConfig> entry : shopifyProperties.getStores().entrySet()) {
            stores.add(Store.builder()
                    .name(entry.getKey())
                    .build());
        }
        
        return stores;
    }

    /**
     * Gets user count information for a store
     *
     * @param storeName the name of the store
     * @return the user count information
     */
    @Override
    public UserCount getUserCount(String storeName) {
        StoreConfig storeConfig = getStoreConfig(storeName);
        
        String url = storeConfig.getUsersVisitedUrl();
        String token = storeConfig.getAccessToken();
        
        log.info("Calling Shopify API - User Count");
        log.info("Request URL: {}", url);
        log.info("Request Headers: X-Shopify-Access-Token: {}", token);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );
            
            JsonNode jsonNode = response.getBody();
            if (jsonNode != null) {
                int count = jsonNode.get("count").asInt();
                return UserCount.builder()
                        .count(count)
                        .lastUpdated(getCurrentTimestamp())
                        .build();
            } else {
                throw new ShopifyApiException("Empty response body from Shopify API");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ShopifyApiException("Error fetching user count: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new ShopifyApiException("Error fetching user count: " + e.getMessage());
        }
    }

    /**
     * Gets order count information for a store
     *
     * @param storeName the name of the store
     * @return the order count information
     */
    @Override
    public OrderCount getOrderCount(String storeName) {
        StoreConfig storeConfig = getStoreConfig(storeName);
        
        String url = storeConfig.getTotalOrdersUrl();
        String token = storeConfig.getAccessToken();
        
        log.info("Calling Shopify API - Order Count");
        log.info("Request URL: {}", url);
        log.info("Request Headers: X-Shopify-Access-Token: {}", token);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );
            
            JsonNode jsonNode = response.getBody();
            if (jsonNode != null) {
                int count = jsonNode.get("count").asInt();
                return OrderCount.builder()
                        .count(count)
                        .lastUpdated(getCurrentTimestamp())
                        .build();
            } else {
                throw new ShopifyApiException("Empty response body from Shopify API");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ShopifyApiException("Error fetching order count: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new ShopifyApiException("Error fetching order count: " + e.getMessage());
        }
    }

    /**
     * Gets cart abandonment information for a store
     *
     * @param storeName the name of the store
     * @return the cart abandonment information
     */
    @Override
    public CartAbandonment getCartAbandonment(String storeName) {
        StoreConfig storeConfig = getStoreConfig(storeName);
        
        String url = storeConfig.getCartAbandonmentUrl();
        String token = storeConfig.getAccessToken();
        
        log.info("Calling Shopify API - Cart Abandonment");
        log.info("Request URL: {}", url);
        log.info("Request Headers: X-Shopify-Access-Token: {}", token);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );
            
            JsonNode jsonNode = response.getBody();
            if (jsonNode != null) {
                int count = jsonNode.get("checkouts").size();
                return CartAbandonment.builder()
                        .count(count)
                        .lastUpdated(getCurrentTimestamp())
                        .build();
            } else {
                throw new ShopifyApiException("Empty response body from Shopify API");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ShopifyApiException("Error fetching cart abandonment: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new ShopifyApiException("Error fetching cart abandonment: " + e.getMessage());
        }
    }

    /**
     * Gets revenue information for a store
     *
     * @param storeName the name of the store
     * @return the revenue information
     */
    @Override
    public Revenue getRevenue(String storeName) {
        StoreConfig storeConfig = getStoreConfig(storeName);
        
        // Check if the revenue URL is available
        if (storeConfig.getTotalRevenueUrl() == null || storeConfig.getTotalRevenueUrl().isEmpty()) {
            return Revenue.builder()
                    .total(0.0f)
                    .currency("N/A")
                    .lastUpdated(getCurrentTimestamp())
                    .build();
        }
        
        String url = storeConfig.getTotalRevenueUrl();
        String token = storeConfig.getAccessToken();
        
        log.info("Calling Shopify API - Revenue");
        log.info("Request URL: {}", url);
        log.info("Request Headers: X-Shopify-Access-Token: {}", token);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );
            
            JsonNode jsonNode = response.getBody();
            if (jsonNode != null) {
                float total = 0.0f;
                String currency = "USD";
                
                if (jsonNode.has("orders") && jsonNode.get("orders").isArray()) {
                    for (JsonNode order : jsonNode.get("orders")) {
                        if (order.has("total_price")) {
                            total += order.get("total_price").asDouble();
                        }
                        if (order.has("currency") && currency.equals("USD")) {
                            currency = order.get("currency").asText();
                        }
                    }
                }
                
                return Revenue.builder()
                        .total(total)
                        .currency(currency)
                        .lastUpdated(getCurrentTimestamp())
                        .build();
            } else {
                throw new ShopifyApiException("Empty response body from Shopify API");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ShopifyApiException("Error fetching revenue: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new ShopifyApiException("Error fetching revenue: " + e.getMessage());
        }
    }

    /**
     * Gets the configuration for a store
     *
     * @param storeName the name of the store
     * @return the store configuration
     * @throws ShopifyApiException if the store is not found
     */
    private StoreConfig getStoreConfig(String storeName) {
        log.debug("Getting store config for: {}", storeName);
        log.debug("Available stores: {}", shopifyProperties.getStores().keySet());
        
        StoreConfig storeConfig = shopifyProperties.getStores().get(storeName);
        if (storeConfig == null) {
            log.error("Store not found: {}", storeName);
            throw new ShopifyApiException("Store not found: " + storeName);
        }
        return storeConfig;
    }

    /**
     * Gets the current timestamp as a formatted string
     *
     * @return the current timestamp
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
