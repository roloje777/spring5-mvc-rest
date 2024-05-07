package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> {
                        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                        customerDTO.setCustomerUrl("/api/v1/customers/" + customer.getId());
                        return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerUrl("/api/v1/customers/" + customer.getId());
                    return customerDTO;
                })
                .orElseThrow(RuntimeException::new); //todo implement better exception handling
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        //create and save the customer
        Customer customer = customerMapper.customerDTOtoCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        // return the newly created Customer DTO object and set the url
        CustomerDTO returnedDTO = customerMapper.customerToCustomerDTO(savedCustomer);
        returnedDTO.setCustomerUrl("/api/v1/customer/"+ savedCustomer.getId());

        return returnedDTO;
    }
}
