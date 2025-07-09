package com.shopify.graphql.exception;

/**
 * Exception thrown when there is an error with the Shopify API
 */
public class ShopifyApiException extends RuntimeException {

    /**
     * Creates a new ShopifyApiException with the specified message
     *
     * @param message the error message
     */
    public ShopifyApiException(String message) {
        super(message);
    }

    /**
     * Creates a new ShopifyApiException with the specified message and cause
     *
     * @param message the error message
     * @param cause   the cause of the exception
     */
    public ShopifyApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
