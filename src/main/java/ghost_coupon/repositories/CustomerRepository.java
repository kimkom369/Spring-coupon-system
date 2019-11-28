package ghost_coupon.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ghost_coupon.entities.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    //-------------- find all customers -----------\\
    @Query("SELECT с FROM Customer с")
    Optional<Collection<Customer>> findAllCustomers();

    //------------------------- find customer by NAME -----------------------------\\
    @Query("SELECT DISTINCT c FROM Customer c WHERE UPPER(c.name) LIKE UPPER(:name)")
    Optional<Customer> findByName(String name);

    //------------------------- find customer by EMAIL ----------------------------\\
    @Query("SELECT DISTINCT c FROM Customer c WHERE UPPER(c.email) LIKE UPPER(:email)")
    Optional<Customer> findByEmail(String email);

    //------------------------------ find customer by NAME & PASSWORD -----------------------------------------\\
    @Query("SELECT DISTINCT c FROM Customer c WHERE UPPER(c.name) LIKE UPPER(:name) AND  c.password = :password")
    Optional<Customer> findByNameAndPassword(String name, String password);

}