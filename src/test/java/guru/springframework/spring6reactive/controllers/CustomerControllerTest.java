package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
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
}
