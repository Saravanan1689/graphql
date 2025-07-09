package com.shopify.graphql.config;

import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.SimpleDataFetcherExceptionHandler;
import graphql.schema.GraphQLScalarType;
import graphql.scalars.ExtendedScalars;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.graphql.execution.DefaultBatchLoaderRegistry;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.util.concurrent.CompletableFuture;

/**
 * Configuration for GraphQL settings
 */
@Configuration
@Slf4j
public class GraphQLConfig {

    /**
     * Configures the RuntimeWiring for GraphQL
     *
     * @return RuntimeWiringConfigurer
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.DateTime);
    }

    /**
     * Creates a BatchLoaderRegistry for efficient query batching
     *
     * @return BatchLoaderRegistry
     */
    @Bean
    public BatchLoaderRegistry batchLoaderRegistry() {
        return new DefaultBatchLoaderRegistry();
    }

    /**
     * Custom exception handler for GraphQL data fetchers
     *
     * @return DataFetcherExceptionHandler
     */
    private DataFetcherExceptionHandler customDataFetcherExceptionHandler() {
        return new DataFetcherExceptionHandler() {
            private final SimpleDataFetcherExceptionHandler defaultHandler = new SimpleDataFetcherExceptionHandler();

            @Override
            public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(
                    DataFetcherExceptionHandlerParameters handlerParameters) {
                Throwable exception = handlerParameters.getException();
                log.error("Error during data fetching", exception);

                // Create a custom GraphQL error with more details
                GraphQLError graphqlError = GraphQLError.newError()
                        .message(exception.getMessage())
                        .path(handlerParameters.getPath())
                        .build();

                // Return a result with the custom error
                DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                        .error(graphqlError)
                        .build();

                return CompletableFuture.completedFuture(result);
            }
        };
    }
}
