package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.mappers.CustomerMapper;
import guru.springframework.spring6reactive.model.CustomerDTO;
import guru.springframework.spring6reactive.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<Void> deleteCustomerById(Integer customerId) {
        return customerRepository.deleteById(customerId);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundCust -> {
                    if(StringUtils.hasText(customerDTO.getCustomerName())){
                        foundCust.setCustomerName(customerDTO.getCustomerName());
                    }
                    return foundCust;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(Integer CustomerId, CustomerDTO customerDTO) {
        return customerRepository.findById(CustomerId)
                .map(foundCust -> {
                    //update properties
                    foundCust.setCustomerName(customerDTO.getCustomerName());

                    return foundCust;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO))
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(Integer CustomerId) {
        return customerRepository.findById(CustomerId)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Flux<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDto);
    }
    
}
