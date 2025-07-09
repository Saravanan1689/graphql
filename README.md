# Shopify GraphQL API

This project provides a GraphQL API that wraps Shopify REST endpoints, allowing for more efficient and flexible data fetching.

## Features

- GraphQL API for Shopify store data
- Support for multiple stores
- Query batching for efficient data fetching
- Comprehensive error handling
- Detailed logging
- GraphiQL interface for API exploration

## Technologies

- Java 21
- Spring Boot 3.5.3
- Spring GraphQL
- Spring WebFlux
- Lombok

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository
2. Build the project:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```

## API Documentation

The GraphQL API provides the following queries:

### Queries

- `store(name: String!)`: Get information about a specific store
- `allStores`: Get information about all available stores

### Types

- `Store`: Represents a Shopify store with various metrics
  - `name`: The name of the store
  - `userCount`: User count information
  - `orderCount`: Order count information
  - `cartAbandonment`: Cart abandonment information
  - `revenue`: Revenue information

- `UserCount`: Information about user counts for a store
  - `count`: The total number of users
  - `lastUpdated`: When this information was last updated

- `OrderCount`: Information about order counts for a store
  - `count`: The total number of orders
  - `lastUpdated`: When this information was last updated

- `CartAbandonment`: Information about cart abandonment for a store
  - `count`: The number of abandoned carts
  - `lastUpdated`: When this information was last updated

- `Revenue`: Information about revenue for a store
  - `total`: The total revenue amount
  - `currency`: The currency of the revenue
  - `lastUpdated`: When this information was last updated

## Example Queries

### Get information about a specific store

```graphql
query {
  store(name: "cocoavia") {
    name
    userCount {
      count
      lastUpdated
    }
    orderCount {
      count
      lastUpdated
    }
    cartAbandonment {
      count
      lastUpdated
    }
    revenue {
      total
      currency
      lastUpdated
    }
  }
}
```

### Get information about all stores

```graphql
query {
  allStores {
    name
    userCount {
      count
    }
    orderCount {
      count
    }
  }
}
```

## GraphiQL Interface

The GraphiQL interface is available at `http://localhost:8080/graphiql` when the application is running. This provides an interactive way to explore and test the GraphQL API.

## Configuration

The application is configured using the `application.yml` file. This includes:

- Server port
- GraphQL settings
- Logging configuration
- Shopify store configurations (access tokens and URLs)

## License

This project is licensed under the MIT License - see the LICENSE file for details.
