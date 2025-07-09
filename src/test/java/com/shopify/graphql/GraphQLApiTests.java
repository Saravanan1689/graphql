package com.shopify.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import com.shopify.graphql.model.Store;
import com.shopify.graphql.model.UserCount;
import com.shopify.graphql.resolver.QueryResolver;
import com.shopify.graphql.service.ShopifyService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Tests for the GraphQL API
 */
@GraphQlTest(QueryResolver.class)
public class GraphQLApiTests {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private ShopifyService shopifyService;

    /**
     * Test the store query
     */
    @Test
    void testStoreQuery() {
        // Given
        String storeName = "testStore";
        Store store = Store.builder()
                .name(storeName)
                .build();
        
        when(shopifyService.getStore(storeName)).thenReturn(store);
        when(shopifyService.getUserCount(storeName)).thenReturn(
                UserCount.builder()
                        .count(100)
                        .lastUpdated("2023-01-01T00:00:00Z")
                        .build()
        );

        // When/Then
        String query = """
                query {
                    store(name: "testStore") {
                        name
                        userCount {
                            count
                            lastUpdated
                        }
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("store.name").entity(String.class).isEqualTo(storeName)
                .path("store.userCount.count").entity(Integer.class).isEqualTo(100)
                .path("store.userCount.lastUpdated").entity(String.class).isEqualTo("2023-01-01T00:00:00Z");
    }

    /**
     * Test the allStores query
     */
    @Test
    void testAllStoresQuery() {
        // Given
        List<Store> stores = Arrays.asList(
                Store.builder().name("store1").build(),
                Store.builder().name("store2").build()
        );
        
        when(shopifyService.getAllStores()).thenReturn(stores);

        // When/Then
        String query = """
                query {
                    allStores {
                        name
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("allStores[0].name").entity(String.class).isEqualTo("store1")
                .path("allStores[1].name").entity(String.class).isEqualTo("store2");
    }
}
