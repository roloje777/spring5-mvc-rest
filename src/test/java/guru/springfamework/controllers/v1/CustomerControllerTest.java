package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends AbstractControllerTest{

    public static final Long ID = 1L;
    public static final String FIRST_NAME = "Jane";
    public static final String LAST_NAME = "Moore";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

    }

    @Test
    public void testGetCustomers()throws Exception{

        //given
        CustomerDTO customer1= new CustomerDTO();
        customer1.setFirstName(FIRST_NAME);
        customer1.setLastName(LAST_NAME);
        customer1.setCustomerUrl("/api/v1/customers/1");

        CustomerDTO customer2= new CustomerDTO();
        customer1.setFirstName("John");
        customer1.setLastName("Snow");
        customer1.setCustomerUrl("/api/v1/customers/2");

       //when

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer1,customer2));

        mockMvc.perform(get("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.customers",hasSize(2)));

    }

    @Test
    public void testGetCustomerById() throws Exception{

        //given
        CustomerDTO customer1= new CustomerDTO();
        customer1.setFirstName(FIRST_NAME);
        customer1.setLastName(LAST_NAME);
        customer1.setCustomerUrl("/api/v1/customers/1");
        when(customerService.getCustomer(anyLong())).thenReturn(customer1);

        //when
        mockMvc.perform(get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",equalTo(FIRST_NAME)));


    }

    @Test
    public void testCreateNewCustomer() throws Exception {
        //given
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("John");
        customer.setLastName("Snow");

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(customer.getFirstName());
        returnDTO.setLastName(customer.getLastName());
        returnDTO.setCustomerUrl("/api/v1/customers/1");

        when(customerService.createNewCustomer(customer)).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(post("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo("John")))
                .andExpect(jsonPath("$.customerUrl", equalTo("/api/v1/customers/1")));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        // tests the Put action
        //given
        // passed in customer DTO
        CustomerDTO passedInCustomer = new CustomerDTO();
        passedInCustomer.setFirstName("John");
        passedInCustomer.setLastName("Wayne");

        //returned DTO
        CustomerDTO returnedCustomer = new CustomerDTO();
        returnedCustomer.setFirstName(passedInCustomer.getFirstName());
        returnedCustomer.setLastName(passedInCustomer.getLastName());
        returnedCustomer.setCustomerUrl("/api/v1/customers/1");

        //when the service layer is called

         when(customerService.updateCustomer(anyLong(),any(CustomerDTO.class))).thenReturn(returnedCustomer);
         //when then mock put
        mockMvc.perform(put("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                //onverts object to JSON Object see AbstractControllerTest.java
                .content(asJsonString(passedInCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",equalTo("John")))
                .andExpect(jsonPath("$.lastName",equalTo("Wayne")))
                .andExpect(jsonPath("$.customerUrl",equalTo("/api/v1/customers/1")));

    }
}
