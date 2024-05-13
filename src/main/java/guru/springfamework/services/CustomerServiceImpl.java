package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.controllers.v1.CustomerController;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
                        customerDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + customer.getId());
                        return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + customer.getId());
                    return customerDTO;
                })
                .orElseThrow(RuntimeException::new); //todo implement better exception handling
    }

    private CustomerDTO saveCustomer(Customer customer){
        // now save the customer
        Customer savedCustomer = customerRepository.save(customer);
        //return the DTO object after setting the url
        CustomerDTO returnedCustomerDTO = customerMapper.customerToCustomerDTO(savedCustomer);
        returnedCustomerDTO.setCustomerUrl(CustomerController.BASE_URL+ "/" + savedCustomer.getId());
        return returnedCustomerDTO;
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        //create and save the customer
        Customer customer = customerMapper.customerDTOtoCustomer(customerDTO);
        return saveCustomer(customer);
    }

//    @Override
//    public CustomerDTO updateCustomer(long id, CustomerDTO customerDTO) {
//
//        if(customerDTO.getFirstName()== null || customerDTO.getFirstName().isEmpty())
//            customerDTO.setFirstName(getCustomer(id).getFirstName());
//        if(customerDTO.getLastName() == null || customerDTO.getLastName().isEmpty())
//            customerDTO.setLastName(getCustomer(id).getLastName());
//
//        //get the customer from DTO and set it's id to id passed in
//        Customer customer = customerMapper.customerDTOtoCustomer(customerDTO);
//        customer.setId(id);
//        return saveCustomer(customer);
//    }
    // Will use method for PUT and PATCH
    @Override
    public CustomerDTO updateCustomer(long id, CustomerDTO customerDTO) {

        return customerRepository.findById(id)
                .map(customer -> {

                    if(customerDTO.getFirstName()!= null && !customerDTO.getFirstName().trim().isEmpty())
                        customer.setFirstName(customerDTO.getFirstName());
                    if(customerDTO.getLastName()!=null && !customerDTO.getLastName().trim().isEmpty())
                        customer.setLastName(customerDTO.getLastName());

                    CustomerDTO returnedCustomerDTO = customerMapper.customerToCustomerDTO(customerRepository.save(customer));
                    returnedCustomerDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + id);
                    return returnedCustomerDTO;

                }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void delete(long id) {
        customerRepository.deleteById(id);
    }


}
