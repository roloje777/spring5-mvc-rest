package guru.springfamework.repositories;

import guru.springfamework.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/*Note that It inherits the JPA nmethods */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
