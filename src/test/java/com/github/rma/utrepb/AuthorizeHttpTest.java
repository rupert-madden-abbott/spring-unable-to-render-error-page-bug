package com.github.rma.utrepb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "authorize-http-requests=false",
                "logging.level.org.springframework.security=TRACE"
        }
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AuthorizeHttpTest {

    private final TestRestTemplate testRestTemplate;

    AuthorizeHttpTest(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    @Test
    void shouldReturnUnauthorizedWhenUnauthenticatedAccessToSecuredEndpoint() {
        ResponseEntity<String> entity = testRestTemplate
                .getForEntity("/secure", String.class);

        assertThat(entity)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnOkWhenAuthenticatedAccessToSecuredEndpoint() {
        ResponseEntity<String> entity = testRestTemplate
                .withBasicAuth(Application.BASIC_AUTH_USERNAME, Application.BASIC_AUTH_PASSWORD)
                .getForEntity("/secure", String.class);

        assertThat(entity)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnOkWhenUnauthenticatedAccessToUnsecuredEndpoint() {
        ResponseEntity<String> entity = testRestTemplate
                .getForEntity("/unsecure", String.class);

        assertThat(entity)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnOkWhenAuthenticatedAccessToUnsecuredEndpoint() {
        ResponseEntity<String> entity = testRestTemplate
                .withBasicAuth(Application.BASIC_AUTH_USERNAME, Application.BASIC_AUTH_PASSWORD)
                .getForEntity("/unsecure", String.class);

        assertThat(entity)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnauthenticatedAccessToUnsecuredErrorEndpoint() {
        ResponseEntity<String> entity = testRestTemplate
                .getForEntity("/unsecure-error", null, String.class);

        assertThat(entity)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
