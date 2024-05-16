package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.mapper.VendorMapperImpl;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorsDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class testVendorServiceImpl {

    @Mock
    VendorService vendorService;

    @Mock
    VendorRepository vendorRepository;

    private static final Long ID = 1L;
    private static final String NAME = "Joao Roderigues LTD";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        vendorService = new VendorServiceImpl(VendorMapper.INSTANCE, vendorRepository);
    }

    @Test
    public void testGetAllVendors(){
        //given
        List<Vendor> vendors = Arrays.asList(new Vendor(),new Vendor());
        when (vendorRepository.findAll()).thenReturn(vendors);

        //when
        VendorsDTO vendorsDTO = vendorService.getAllVendors();

        //then
        assertEquals(2, vendorsDTO.getVendors().size());



    }

    @Test
    public void testGetAvendor(){
        //given
        Vendor vendor = new Vendor(ID,NAME);

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));
        //when
        VendorDTO vendorDTO =  vendorService.getAvendor(1L);

        //then
        Assert.assertEquals(NAME, vendorDTO.getName());
        Assert.assertEquals(("/api/v1/vendors/1"),vendorDTO.getVendorUrl());
    }

    @Test(expected= RuntimeException.class)
    public void testGetVendorNotFound() throws RuntimeException{
        //given

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        VendorDTO vendorDTO =  vendorService.getAvendor(anyLong());

        //then
        Assert.assertEquals(NAME, vendorDTO.getName());


    }

    @Test
    public void testCreateAnewVendor(){
        //given
        VendorDTO  vendorDTO = new VendorDTO(NAME, "/api/v1/vendors/1");
        Vendor savedVendor = VendorMapper.INSTANCE.VendorDTOtoVendor(vendorDTO);
        savedVendor.setId(1L);
        when(vendorRepository.save(ArgumentMatchers.any(Vendor.class))).thenReturn(savedVendor);

        //when
        VendorDTO returnedDTO = vendorService.createAnewVendor(vendorDTO);

        //then
        assertEquals(NAME, returnedDTO.getName());
        assertEquals("/api/v1/vendors/1", returnedDTO.getVendorUrl());

    }

    @Test
    public void testUpdateVendor(){
        //given
        VendorDTO updateVendorInfo= new VendorDTO();
        updateVendorInfo.setName("New Vendor");
        Vendor foundVendor = new Vendor(1L,"Existing Vendor");

        //when
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(foundVendor));
        when(vendorRepository.save(foundVendor)).thenReturn(foundVendor);

        VendorDTO returnedDTO = vendorService.updateVendor(1L, updateVendorInfo);

        //the
        assertEquals("New Vendor", returnedDTO.getName());
        assertEquals("/api/v1/vendors/1", returnedDTO.getVendorUrl());

    }

    @Test(expected = RuntimeException.class)
    public void testVendorThrowsException() throws Exception{


        when(vendorRepository.findById(anyLong())).thenThrow(RuntimeException.class);
//         throw new Exception();
        vendorService.updateVendor(1L,null);

    }


    @Test
    public void testDeleteVendor(){
        //when
       vendorService.delete(1L);

       //then
        verify(vendorRepository, times(1)).deleteById(anyLong());


    }



}
