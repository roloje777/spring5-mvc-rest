package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorsDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService{

    private final VendorMapper vendorMapper;
    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorMapper vendorMapper, VendorRepository vendorRepository) {
        this.vendorMapper = vendorMapper;
        this.vendorRepository = vendorRepository;
    }


    @Override
    public VendorsDTO getAllVendors() {
             List<VendorDTO> vendors = vendorRepository.findAll()
                      .stream()
                      .map(vendor -> {
                          VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                          vendorDTO.setVendorUrl(VendorController.VENDOR_URL + "/" + vendor.getId());
                          return vendorDTO;
                      })
                     .collect(Collectors.toList());
        return new VendorsDTO(vendors);
    }

    @Override
    public VendorDTO getAvendor(Long id) {

        Vendor vendor = vendorRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
        vendorDTO.setVendorUrl(VendorController.VENDOR_URL + "/" + id);
        return vendorDTO;
    }

    @Override
    public VendorDTO createAnewVendor(VendorDTO vendorDTO) {
         Vendor vendor= vendorRepository.save(vendorMapper.VendorDTOtoVendor(vendorDTO));
         VendorDTO returnedDTO = vendorMapper.vendorToVendorDTO(vendor);
         returnedDTO.setVendorUrl(VendorController.VENDOR_URL + "/" + vendor.getId());

        return  returnedDTO;
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {
        if(vendorDTO == null || vendorDTO.getName().trim().isEmpty())
            throw new IllegalArgumentException();
        Optional<Vendor> op = vendorRepository.findById(id);
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        vendor.setName(vendorDTO.getName());
       Vendor savedVendor =  vendorRepository.save(vendor);

        VendorDTO returnedDTO = vendorMapper.vendorToVendorDTO(savedVendor);
        returnedDTO.setVendorUrl(VendorController.VENDOR_URL + "/" + id);

        return returnedDTO;
    }

    @Override
    public void delete(Long id) {
         vendorRepository.deleteById(id);
    }
}
