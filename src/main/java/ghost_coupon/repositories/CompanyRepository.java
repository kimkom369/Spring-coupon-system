package ghost_coupon.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ghost_coupon.entities.Company;

/* Annotations:
 
 * @Repository: used on Java classes which directly access the database.
 
 * @Query: define SQL to execute for a Spring Data repository method
 */


@Repository 
public interface CompanyRepository extends JpaRepository<Company, Long> {

    //------------ COMPANY: find all -------------\\
    @Query("SELECT с FROM Company с")
    Optional<Collection<Company>> findAllCompanies();

    //----------------------- COMPANY: find by NAME ------------------------------\\
    @Query("SELECT DISTINCT c FROM Company c WHERE UPPER(c.name) LIKE UPPER(:name)")
    Optional<Company> findByName(String name);

    //----------------------- COMPANY: find by EMAIL -------------------------------\\
    @Query("SELECT DISTINCT c FROM Company c WHERE UPPER(c.email) LIKE UPPER(:email)")
    Optional<Company> findByEmail(String email);

    //------------------------------ COMPANY: find all by NAME & PASSWORD -----------------------------------\\
    @Query("SELECT DISTINCT c FROM Company c WHERE UPPER(c.name) LIKE UPPER(:name) AND c.password = :password")
    Optional<Company> findByNameAndPassword(String name, String password);

}
