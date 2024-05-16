package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorsDTO;
import guru.springfamework.controllers.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static guru.springfamework.controllers.v1.AbstractControllerTest.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {VendorController.class})
public class VendorControllerTest {

    @MockBean //provided by Spring Context
    VendorService vendorService;

    @Autowired
    MockMvc mockMvc; //provided by Spring Context

    VendorDTO vendorDTO_1;
    VendorDTO vendorDTO_2;

    @Before
    public void setup(){
        vendorDTO_1 = new VendorDTO("Franks Fruit Suppliers", VendorController.VENDOR_URL+"/1");
        vendorDTO_2 = new VendorDTO("Coca Cola LTD PTY", VendorController.VENDOR_URL+"/2");
    }

    @Test
    public void testGetAllVendors() throws Exception {
        //given
        VendorsDTO vendors = new VendorsDTO(Arrays.asList(vendorDTO_1,vendorDTO_2));

        //when
        when(vendorService.getAllVendors()).thenReturn(vendors);

        //then
        mockMvc.perform(get(VendorController.VENDOR_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));


    }

@Test
    public void testGetAVendor() throws Exception {

        //given


        //when
        when(vendorService.getAvendor(1L)).thenReturn(vendorDTO_1);

        //then
        mockMvc.perform(get("/api/v1/vendors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Franks Fruit Suppliers" )))
                .andExpect(jsonPath("$.vendor_url",equalTo("/api/v1/vendors/1")));
}

@Test
public void testCreateNewVendor() throws Exception {
        // given
    VendorDTO sentDTO = new VendorDTO();
    sentDTO.setName("Test Vendor");
    VendorDTO returnedDTO = new VendorDTO(sentDTO.getName(),"/api/v1/vendors/5");


    when(vendorService.createAnewVendor(sentDTO)).thenReturn(returnedDTO);

    //when then
    mockMvc.perform(post("/api/v1/vendors/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(sentDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name",equalTo("Test Vendor")))
            .andExpect(jsonPath("$.vendor_url",equalTo("/api/v1/vendors/5")));

}

@Test
    public void testUpdateVendor() throws Exception {
    // given
    VendorDTO sentDTO = new VendorDTO();
    sentDTO.setName("Updated Vendor");
    VendorDTO returnedDTO = new VendorDTO(sentDTO.getName(),"/api/v1/vendors/1");


    when(vendorService.updateVendor(1L,sentDTO)).thenReturn(returnedDTO);

    //when then
    mockMvc.perform(put("/api/v1/vendors/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(sentDTO)))
            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.name",equalTo("Updated Vendor")))
 .andExpect(jsonPath("$.vendor_url",equalTo("/api/v1/vendors/1")));

    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/vendors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vendorService).delete(anyLong());

    }


}
