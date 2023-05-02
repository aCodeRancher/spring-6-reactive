package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.Customer;
import guru.springframework.spring6reactive.model.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    void testListCustomers(){
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange().expectStatus().isOk()
                .expectBodyList(Customer.class).hasSize(3)
                .consumeWith(consumer -> {
                      List<Customer> customerList = consumer.getResponseBody();
                      assertTrue(customerList.get(0).getCustomerName().equals("Customer 1"));
                      assertTrue(customerList.get(1).getCustomerName().equals("Customer 2"));
                      assertTrue(customerList.get(2).getCustomerName().equals("Customer 3"));
                });
    }

    @Test
    @Order(2)
    void testGetById() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class).value(dto->assertTrue(dto.getCustomerName().equals("Customer 1")));
    }

    @Test
    @Order(3)
    void testCreateCustomer() {
        Customer testCust = Customer.builder().customerName("Customer 4").build();
        webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(testCust), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/4");
    }

    @Test
    @Order(4)
    void testUpdateCustomer() {
        Customer updated = Customer.builder().customerName("Customer one").build();
        webTestClient.put()
                .uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(updated), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    void testPatchCustomer() {
        Customer  patched = Customer.builder().customerName("Customer One").build();
        webTestClient.patch()
                .uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(patched), CustomerDTO.class)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    @Order(6)
    void testDeleteCustomer() {
        webTestClient.delete()
                .uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}
