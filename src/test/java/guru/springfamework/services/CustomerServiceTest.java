package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.apache.tomcat.jni.Mmap.delete;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {


    @Mock
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    private static final Long ID = 3L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "James";

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE,customerRepository);

    }
    @Test
    public void testGetAllCustomers(){

        //given
        List<Customer> customers = Arrays.asList(new Customer(), new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        //when
        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();


        //then
        assertEquals(2,customerDTOS.size());
    }

    @Test
    public void testGetCustomerById(){

        //given
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);



        when(customerRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(customer));

        //when
        CustomerDTO customerDTO = customerService.getCustomer(ID);

        //then
        assertEquals(FIRST_NAME, customerDTO.getFirstName());
        assertEquals(LAST_NAME, customerDTO.getLastName());
        assertNotEquals("BOB",customerDTO.getFirstName());

    }

    @Test
    public void testCreateNewCustomer(){
        // given
        // we create a dummy customerDTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Snow");

        // we create the saved customer
        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstName());
        savedCustomer.setLastName(customerDTO.getLastName());
        savedCustomer.setId(1l);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        //when
        CustomerDTO savedDTO = customerService.createNewCustomer(customerDTO);

        //then

        assertEquals(customerDTO.getFirstName(),savedDTO.getFirstName());
        assertEquals(customerDTO.getLastName(),savedDTO.getLastName());
        assertEquals("/api/v1/customers/1",savedDTO.getCustomerUrl());
    }

    @Test
    public void testUpdateCustomer(){
        // given
        // we create a dummy customerDTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("John");
        customerDTO.setLastName("James");

        // existing customer in repository
        Customer existingCustomer = new Customer();
        existingCustomer.setFirstName("Robert");
        existingCustomer.setLastName("James");
        existingCustomer.setId(1L);

        //saved Customer
        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstName());
        savedCustomer.setLastName(existingCustomer.getLastName());
        savedCustomer.setId(1l);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        //when

        CustomerDTO returnedDTO = customerService.updateCustomer(1L,customerDTO);

        //then
        assertEquals(returnedDTO.getFirstName(),"John");
        assertEquals(returnedDTO.getLastName(),"James");
        assertNotNull(returnedDTO.getFirstName());
        assertNotNull(returnedDTO.getLastName());
        assertNotEquals(returnedDTO.getFirstName(),"");
        assertNotEquals(returnedDTO.getLastName(),"");
        assertEquals("/api/v1/customers/1",returnedDTO.getCustomerUrl());


    }

    @Test
    public void test_patch_Customer() {
        // given
        // we create a dummy customerDTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("John");


        // existing customer in repository
        Customer existingCustomer = new Customer();
        existingCustomer.setFirstName("Robert");
        existingCustomer.setLastName("James");
        existingCustomer.setId(1L);

        //saved Customer
        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstName());
        savedCustomer.setLastName(existingCustomer.getLastName());
        savedCustomer.setId(1l);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        //when

        CustomerDTO returnedDTO = customerService.updateCustomer(1L,customerDTO);

        //then
        assertEquals(returnedDTO.getFirstName(),"John");
        assertEquals(returnedDTO.getLastName(),"James");
        assertNotNull(returnedDTO.getFirstName());
        assertNotNull(returnedDTO.getLastName());
        assertNotEquals(returnedDTO.getFirstName(),"");
        assertNotEquals(returnedDTO.getLastName(),"");
        assertEquals("/api/v1/customers/1",returnedDTO.getCustomerUrl());


    }

    @Test
    public void testDelete(){
        long id = 0;
        customerService.delete(id);
        // verifies that the dlete method was called once
        // see https://www.baeldung.com/mockito-verify
        verify(customerRepository, times(1)).deleteById(anyLong());
    }
}
