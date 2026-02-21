package com.bookshop.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de non-régression API (sans auth).
 * Pour la checklist complète (auth, panier, admin), voir CHECKLIST_TESTS.md.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Nested
    @DisplayName("1.2 Public et health")
    class PublicAndHealth {

        @Test
        @DisplayName("1 - Health check")
        void health() {
            ResponseEntity<Map> r = restTemplate.getForEntity(baseUrl() + "/actuator/health", Map.class);
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(r.getBody()).containsEntry("status", "UP");
        }

        @Test
        @DisplayName("2 - Swagger UI")
        void swaggerUi() {
            ResponseEntity<String> r = restTemplate.getForEntity(baseUrl() + "/swagger-ui.html", String.class);
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(r.getBody()).containsIgnoringCase("swagger");
        }

        @Test
        @DisplayName("3 - Catégories")
        void categories() {
            ResponseEntity<List> r = restTemplate.getForEntity(baseUrl() + "/api/public/categories", List.class);
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(r.getBody()).isNotEmpty();
        }

        @Test
        @DisplayName("4 - Liste livres paginée")
        void booksPaginated() {
            ResponseEntity<Map> r = restTemplate.getForEntity(
                    baseUrl() + "/api/public/books?page=0&size=10", Map.class);
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(r.getBody()).containsKey("content");
            List<?> content = (List<?>) r.getBody().get("content");
            if (!content.isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> first = (Map<String, Object>) content.get(0);
                assertThat(first).containsKey("categoryName");
            }
        }

        @Test
        @DisplayName("5 - Détail livre")
        void bookById() {
            ResponseEntity<Map> r = restTemplate.getForEntity(baseUrl() + "/api/public/books/1", Map.class);
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(r.getBody()).containsKey("title");
        }

        @Test
        @DisplayName("6 - Livre inexistant 404")
        void bookNotFound() {
            ResponseEntity<Map> r = restTemplate.getForEntity(baseUrl() + "/api/public/books/99999", Map.class);
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("1.3 Auth (accès public)")
    class Auth {

        @Test
        @DisplayName("10 - GET /api/auth/login pas 403")
        void getAuthLogin() {
            ResponseEntity<String> r = restTemplate.getForEntity(baseUrl() + "/api/auth/login", String.class);
            assertThat(r.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
