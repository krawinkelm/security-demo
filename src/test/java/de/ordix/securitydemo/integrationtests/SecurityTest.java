package de.ordix.securitydemo.integrationtests;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void withoutAuthenticationTest() {
        ResponseEntity<String> response = restTemplate.getForEntity(buildUrl("/without-authentication"), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("Without Authentication"));
    }

    @Test()
    void missingBasicAuthenticationTest() {
        HttpClientErrorException.Unauthorized exception = assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
            restTemplate.getForEntity(buildUrl("/basic-authentication"), String.class);
        });
        assertThat(exception.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void withBasicAuthenticationTest() {
        HttpEntity<String> httpEntity = new HttpEntity<>(createBasicAuthentication("demo", "demo123"));
        ResponseEntity<String> response = restTemplate.exchange(buildUrl("/basic-authentication"), HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("Basic Authentication"));
    }

    @Test()
    void missingFilterAuthenticationTest() {
        HttpClientErrorException.Unauthorized exception = assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
            restTemplate.getForEntity(buildUrl("/filter-authentication"), String.class);
        });
        assertThat(exception.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test()
    void withFilterAuthenticationTest() {
        HttpEntity<String> httpEntity = new HttpEntity<>(createFilterAuthentication("demo123"));
        ResponseEntity<String> response = restTemplate.exchange(buildUrl("/filter-authentication"), HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("Filter Authentication"));
    }


    private HttpHeaders createBasicAuthentication(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    private HttpHeaders createFilterAuthentication(String token) {
        return new HttpHeaders() {
            {
                set("Authorization", "Bearer " + token);
            }
        };
    }

    private URI buildUrl(String path) {
        String url = String.format("http://localhost:%d/demo%s", port, path);
        return URI.create(url);
    }
}
