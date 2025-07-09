package com.shopify.graphql.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shopify.graphql.config.ShopifyProperties;
import com.shopify.graphql.config.ShopifyProperties.StoreConfig;
import com.shopify.graphql.exception.ShopifyApiException;
import com.shopify.graphql.model.CartAbandonment;
import com.shopify.graphql.model.OrderCount;
import com.shopify.graphql.model.Revenue;
import com.shopify.graphql.model.Store;
import com.shopify.graphql.model.UserCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Tests for the ShopifyServiceImpl class
 */
@ExtendWith(MockitoExtension.class)
public class ShopifyServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;
    private ShopifyServiceImpl shopifyService;
    private ShopifyProperties shopifyProperties;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        shopifyProperties = new ShopifyProperties();
        
        Map<String, StoreConfig> stores = new HashMap<>();
        
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setAccessToken("test-token");
        storeConfig.setUsersVisitedUrl("https://test-store.myshopify.com/admin/api/customers/count.json");
        storeConfig.setTotalOrdersUrl("https://test-store.myshopify.com/admin/api/orders/count.json?status=any");
        storeConfig.setCartAbandonmentUrl("https://test-store.myshopify.com/admin/api/checkouts.json?limit=1");
        storeConfig.setTotalRevenueUrl("https://test-store.myshopify.com/admin/api/orders.json?status");
        
        stores.put("testStore", storeConfig);
        shopifyProperties.setStores(stores);
        
        shopifyService = new ShopifyServiceImpl(restTemplate, objectMapper, shopifyProperties);
    }

    @Test
    void testGetStore() {
        // When
        Store store = shopifyService.getStore("testStore");
        
        // Then
        assertNotNull(store);
        assertEquals("testStore", store.getName());
    }

    @Test
    void testGetStoreNotFound() {
        // When/Then
        assertThrows(ShopifyApiException.class, () -> shopifyService.getStore("nonExistentStore"));
    }

    @Test
    void testGetAllStores() {
        // When
        List<Store> stores = shopifyService.getAllStores();
        
        // Then
        assertNotNull(stores);
        assertEquals(1, stores.size());
        assertEquals("testStore", stores.get(0).getName());
    }

    @Test
    void testGetUserCount() {
        // Given
        ObjectNode responseJson = JsonNodeFactory.instance.objectNode();
        responseJson.put("count", 42);
        
        setupRestTemplateMock(responseJson);
        
        // When
        UserCount userCount = shopifyService.getUserCount("testStore");
        
        // Then
        assertNotNull(userCount);
        assertEquals(42, userCount.getCount());
        assertNotNull(userCount.getLastUpdated());
    }

    @Test
    void testGetOrderCount() {
        // Given
        ObjectNode responseJson = JsonNodeFactory.instance.objectNode();
        responseJson.put("count", 100);
        
        setupRestTemplateMock(responseJson);
        
        // When
        OrderCount orderCount = shopifyService.getOrderCount("testStore");
        
        // Then
        assertNotNull(orderCount);
        assertEquals(100, orderCount.getCount());
        assertNotNull(orderCount.getLastUpdated());
    }

    @Test
    void testGetCartAbandonment() {
        // Given
        ObjectNode responseJson = JsonNodeFactory.instance.objectNode();
        ArrayNode checkouts = JsonNodeFactory.instance.arrayNode();
        checkouts.add(JsonNodeFactory.instance.objectNode());
        checkouts.add(JsonNodeFactory.instance.objectNode());
        responseJson.set("checkouts", checkouts);
        
        setupRestTemplateMock(responseJson);
        
        // When
        CartAbandonment cartAbandonment = shopifyService.getCartAbandonment("testStore");
        
        // Then
        assertNotNull(cartAbandonment);
        assertEquals(2, cartAbandonment.getCount());
        assertNotNull(cartAbandonment.getLastUpdated());
    }

    @Test
    void testGetRevenue() {
        // Given
        ObjectNode responseJson = JsonNodeFactory.instance.objectNode();
        ArrayNode orders = JsonNodeFactory.instance.arrayNode();
        
        ObjectNode order1 = JsonNodeFactory.instance.objectNode();
        order1.put("total_price", 100.50);
        order1.put("currency", "USD");
        
        ObjectNode order2 = JsonNodeFactory.instance.objectNode();
        order2.put("total_price", 200.75);
        order2.put("currency", "USD");
        
        orders.add(order1);
        orders.add(order2);
        responseJson.set("orders", orders);
        
        setupRestTemplateMock(responseJson);
        
        // When
        Revenue revenue = shopifyService.getRevenue("testStore");
        
        // Then
        assertNotNull(revenue);
        assertEquals(301.25f, revenue.getTotal(), 0.01);
        assertEquals("USD", revenue.getCurrency());
        assertNotNull(revenue.getLastUpdated());
    }

    private void setupRestTemplateMock(JsonNode responseJson) {
        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(JsonNode.class)
        )).thenReturn(responseEntity);
    }
}
