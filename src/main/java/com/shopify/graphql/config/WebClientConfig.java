package com.shopify.graphql.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

/**
 * Configuration for RestTemplate used to make REST API calls to Shopify
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebClientConfig {

    private final ShopifyProperties shopifyProperties;

    /**
     * Creates a RestTemplate bean with configured timeouts and logging
     *
     * @return configured RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        int connectTimeout = shopifyProperties.getClient().getConnectTimeout();
        int readTimeout = shopifyProperties.getClient().getReadTimeout();
        
        log.debug("Configuring RestTemplate with timeouts: connect={}, read={}", 
                connectTimeout, readTimeout);
        
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .additionalInterceptors(new LoggingInterceptor())
                .build();
    }

    /**
     * Logging interceptor for RestTemplate requests and responses
     */
    private static class LoggingInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            // Log request
            log.debug("============ Request Details ============");
            log.debug("Request Method: {}", request.getMethod());
            log.debug("Request URL: {}", request.getURI());
            log.debug("Request Headers:");
            request.getHeaders().forEach((name, values) -> 
                values.forEach(value -> log.debug("    {}: {}", name, value))
            );
            
            if (body.length > 0) {
                log.debug("Request has body (not logged for security reasons)");
            }
            log.debug("=======================================");
            
            // Execute request
            ClientHttpResponse response = execution.execute(request, body);
            
            // Log response
            log.debug("============ Response Details ============");
            log.debug("Response Status: {}", response.getStatusCode());
            log.debug("Response Headers:");
            response.getHeaders().forEach((name, values) -> 
                values.forEach(value -> log.debug("    {}: {}", name, value))
            );
            
            log.debug("Response body available: {}", response.getStatusCode().is2xxSuccessful());
            log.debug("=======================================");
            
            return response;
        }
    }
}
