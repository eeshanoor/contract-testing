package com.eeshanoor.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@Provider("UserProvider")
@PactFolder("src/test/resources/pacts")
public class UserProviderPactTest {

    @BeforeEach
    void setUp(PactVerificationContext context) {
        // Point to the real provider (reqres.in acts as our provider for demo)
        context.setTarget(new HttpTestTarget("reqres.in", 443, true));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("user with ID 1 exists")
    void userWithId1Exists() {
        // In a real app: seed test data to DB or configure mock
        System.out.println("State: user with ID 1 exists — using reqres.in live data");
    }

    @State("user with ID 999 does not exist")
    void userWithId999DoesNotExist() {
        System.out.println("State: user 999 does not exist — reqres.in returns 404");
    }

    @State("a user creation request")
    void userCreationRequest() {
        System.out.println("State: provider ready to accept POST /api/users");
    }
}