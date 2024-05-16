package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VendorMapperTest {
 private static final Long ID = 1L;
 private static final String NAME = "Exotic Fruits LLC";

 VendorMapper vendorMapper = VendorMapper.INSTANCE;

 @Test
 public void vendorToVendorDTOtest(){
  // given
  Vendor vendor = new Vendor(ID,NAME);

  //when
  VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);

  //then
  assertEquals(NAME, vendorDTO.getName());

 }

 @Test
 public void vendorDTOtoVendorTest(){

  // given
  VendorDTO vendorDTO = new VendorDTO(NAME,"/api/v1/vendors/1");

  //when
  Vendor vendor = vendorMapper.VendorDTOtoVendor(vendorDTO);

  //then
  assertEquals(NAME, vendor.getName());

 }

}
