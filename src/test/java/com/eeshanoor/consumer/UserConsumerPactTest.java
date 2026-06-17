package com.eeshanoor.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "UserProvider")
public class UserConsumerPactTest {

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact getUserByIdPact(PactDslWithProvider builder) {
        return builder
            .given("user with ID 1 exists")
            .uponReceiving("a request to get user by ID")
                .path("/api/users/1")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                    .object("data")
                        .integerType("id", 1)
                        .stringType("email", "george.bluth@reqres.in")
                        .stringType("first_name", "George")
                        .stringType("last_name", "Bluth")
                    .closeObject()
                )
            .toPact();
    }

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact getUserNotFoundPact(PactDslWithProvider builder) {
        return builder
            .given("user with ID 999 does not exist")
            .uponReceiving("a request to get non-existent user")
                .path("/api/users/999")
                .method("GET")
            .willRespondWith()
                .status(404)
            .toPact();
    }

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createUserPact(PactDslWithProvider builder) {
        return builder
            .given("a user creation request")
            .uponReceiving("a POST request to create a user")
                .path("/api/users")
                .method("POST")
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                    .stringType("name", "Eesha Noor")
                    .stringType("job", "SDET")
                )
            .willRespondWith()
                .status(201)
                .body(new PactDslJsonBody()
                    .stringType("name", "Eesha Noor")
                    .stringType("job", "SDET")
                    .stringType("id")
                    .stringType("createdAt")
                )
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getUserByIdPact")
    void testGetUserById(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();
        Response response = given().get("/api/users/1");
        assertEquals(200, response.statusCode());
        assertNotNull(response.jsonPath().getMap("data"));
        assertEquals(1, response.jsonPath().getInt("data.id"));
        assertNotNull(response.jsonPath().getString("data.email"));
    }

    @Test
    @PactTestFor(pactMethod = "getUserNotFoundPact")
    void testGetUserNotFound(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();
        Response response = given().get("/api/users/999");
        assertEquals(404, response.statusCode());
    }

    @Test
    @PactTestFor(pactMethod = "createUserPact")
    void testCreateUser(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();
        Response response = given()
            .contentType("application/json")
            .body("{\"name\":\"Eesha Noor\",\"job\":\"SDET\"}")
            .post("/api/users");
        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
        assertEquals("Eesha Noor", response.jsonPath().getString("name"));
    }
}