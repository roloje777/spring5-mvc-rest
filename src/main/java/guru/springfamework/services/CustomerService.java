package guru.springfamework.services;

import guru.springfamework.api.v1.model.CustomerDTO;

import java.util.List;


public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomer(Long id);

    CustomerDTO createNewCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(long id, CustomerDTO customerDTO);

//    CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO);
    void delete(long id);
}
