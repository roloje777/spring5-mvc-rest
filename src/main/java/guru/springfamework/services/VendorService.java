package guru.springfamework.services;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorsDTO;
import guru.springfamework.domain.Vendor;

import java.util.List;

public interface VendorService {

    VendorsDTO getAllVendors();

    VendorDTO getAvendor(Long id);

    VendorDTO createAnewVendor(VendorDTO vendorDTO);

    VendorDTO updateVendor(Long id, VendorDTO vendorDTO);

    void delete(Long id);
}
