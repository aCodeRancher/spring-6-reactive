package guru.springframework.spring6reactive.repositories;

import guru.springframework.spring6reactive.config.DatabaseConfig;
import guru.springframework.spring6reactive.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import(DatabaseConfig.class)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void saveNewCustomer() {
        customerRepository.save(getTestCustomer())
                .subscribe(beer -> {
                    System.out.println(beer.toString());
                });
    }

    Customer getTestCustomer() {
        return Customer.builder()
                .customerName("John Smith")
               .build();
    }
}
