# Contract Testing Framework
> **Eesha Noor** | SDET | Java + Pact + JUnit 5

## What is Contract Testing?
Contract testing verifies that microservices can communicate by testing agreements (contracts) between consumers and providers — without full integration tests.

## Tech Stack
- Java 11
- Pact JVM 4.x (Consumer-driven contract testing)
- JUnit 5
- Maven
- Pact Broker (CI integration)
- Spring Boot (Provider mock)

## Flow
```
Consumer writes test → Pact file generated → Provider verifies Pact
     (UserConsumer)         (pacts/*.json)        (UserProvider)
```

## Run Tests
```bash
# Consumer tests — generate pact files
mvn test -Dtest=UserConsumerPactTest

# Provider verification — verify against pact files
mvn test -Dtest=UserProviderPactTest

# Publish pacts to broker
mvn pact:publish
```