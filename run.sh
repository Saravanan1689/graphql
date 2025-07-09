#!/bin/bash

# Script to build and run the Shopify GraphQL API

# Function to display help
show_help() {
    echo "Usage: ./run.sh [OPTION]"
    echo "Build and run the Shopify GraphQL API"
    echo ""
    echo "Options:"
    echo "  build       Build the application"
    echo "  run         Run the application"
    echo "  docker      Build and run with Docker"
    echo "  test        Run tests"
    echo "  clean       Clean the project"
    echo "  help        Display this help message"
    echo ""
}

# Function to build the application
build_app() {
    echo "Building the application..."
    ./mvnw clean package -DskipTests
    echo "Build complete."
}

# Function to run the application
run_app() {
    echo "Running the application..."
    ./mvnw spring-boot:run
}

# Function to build and run with Docker
docker_run() {
    echo "Building and running with Docker..."
    docker-compose up --build
}

# Function to run tests
run_tests() {
    echo "Running tests..."
    ./mvnw test
}

# Function to clean the project
clean_project() {
    echo "Cleaning the project..."
    ./mvnw clean
    echo "Clean complete."
}

# Check if no arguments were provided
if [ $# -eq 0 ]; then
    show_help
    exit 1
fi

# Process command line arguments
case "$1" in
    build)
        build_app
        ;;
    run)
        run_app
        ;;
    docker)
        docker_run
        ;;
    test)
        run_tests
        ;;
    clean)
        clean_project
        ;;
    help)
        show_help
        ;;
    *)
        echo "Unknown option: $1"
        show_help
        exit 1
        ;;
esac

exit 0
