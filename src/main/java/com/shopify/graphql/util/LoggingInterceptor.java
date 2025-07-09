package com.shopify.graphql.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Interceptor for logging HTTP requests and responses
 */
@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    /**
     * Intercepts HTTP requests and responses for logging
     *
     * @param request   the HTTP request
     * @param body      the request body
     * @param execution the execution chain
     * @return the HTTP response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    /**
     * Logs the HTTP request
     *
     * @param request the HTTP request
     * @param body    the request body
     */
    private void logRequest(HttpRequest request, byte[] body) {
        if (log.isDebugEnabled()) {
            log.debug("============================= Request ==============================");
            log.debug("URI: {}", request.getURI());
            log.debug("Method: {}", request.getMethod());
            log.debug("Headers: {}", request.getHeaders());
            log.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));
            log.debug("==================================================================");
        }
    }

    /**
     * Logs the HTTP response
     *
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs
     */
    private void logResponse(ClientHttpResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("============================= Response =============================");
            log.debug("Status code: {}", response.getStatusCode());
            log.debug("Status text: {}", response.getStatusText());
            log.debug("Headers: {}", response.getHeaders());
            log.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
            log.debug("==================================================================");
        }
    }
}
