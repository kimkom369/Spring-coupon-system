package ghost_coupon.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ghost_coupon.entities.Income;


@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
	
	
    //------------ delete expired incomes ----------------------\\
    @Modifying
    @Transactional
    @Query("DELETE FROM Income i WHERE i.expDate < CURRENT_DATE")
    void deleteExpiredIncomes();


    //------------ find all incomes ------------\\
	@Query("SELECT i FROM Income i")
    Optional<Collection<Income>> findAllIncomes();

    //------------ COMPANY: find company incomes -----------------\\
    @Query("SELECT i FROM Income i WHERE i.company.id = :companyId")
    Optional<Collection<Income>> findCompanyIncomes(long companyId);

    //--------------- CUSTOMER: find customer incomes -------------\\
    @Query("SELECT i FROM Income i WHERE i.customer.id = :companyId")
    Optional<Collection<Income>> findCustomerIncomes(long companyId);

}
