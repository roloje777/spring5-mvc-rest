package guru.springfamework.bootstrap;

import guru.springfamework.domain.Category;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 9/24/17.
 */
@Component
public class Bootstrap implements CommandLineRunner{

    private CategoryRepository categoryRespository;
    private CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRespository, CustomerRepository customerRepository,
                     VendorRepository vendorRepository) {

        this.categoryRespository = categoryRespository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadFruits();
        loadCustomers();
        loadVendors();


    }

    private void loadVendors() {
        vendorRepository.save(new Vendor("Franks Depo PTY"));
        vendorRepository.save(new Vendor("Welkom Coca Cola LTD PTY"));
        System.out.println("Vendors Loaded = " + vendorRepository.count() );

    }

    private void loadCustomers() {
       customerRepository.save(new Customer("Freddy","Meyers"));
        customerRepository.save(new Customer("Joe","Buck"));
        customerRepository.save(new Customer("Micheal","Weston"));

        System.out.println("Customers Loaded = " + categoryRespository.count() );

    }

    private void loadFruits() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRespository.save(fruits);
        categoryRespository.save(dried);
        categoryRespository.save(fresh);
        categoryRespository.save(exotic);
        categoryRespository.save(nuts);


        System.out.println("Fruits Loaded = " + categoryRespository.count() );
    }
}
