package ghost_coupon.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ghost_coupon.entities.Coupon;
import ghost_coupon.enums.CouponType;

/* Annotations:

 * @Repository: used on Java classes which directly access the database.
 
 * @Query: define SQL to execute for a Spring Data repository method
 
 * @Modifiying:
 * 
 * @Transactional:
 */


@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
	
//==================================================== COUPON queries ========================================================\\
    
	//--------------------------- COUPON: find by TITLE --------------------------\\
    @Query("SELECT DISTINCT c FROM Coupon c WHERE UPPER(c.title) LIKE UPPER(:title)")
    Optional<Coupon> findByTitle(String title);
    
    //------------- COUPON: find ALL -----------\\
    @Query("SELECT с FROM Coupon с")
    Optional<Collection<Coupon>> findAllCoupons();
    
    //------------- COUPON: find all available ----------\\
    @Query("SELECT c FROM Coupon c WHERE c.amount > 0")
    Optional<Collection<Coupon>> findAllAvailableCoupons();
    
    
    //--------------- COUPON: delete expired coupon ------------\\
    @Modifying
    @Transactional
    @Query("DELETE FROM Coupon c WHERE c.endDate < CURRENT_DATE")
    void deleteExpiredCoupons();

    
//=================================================== COMPANY queries =========================================================\\
    
    //------------------------ COMPANY: find coupon -----------------------------------\\
    @Query("SELECT c FROM Coupon c WHERE c.company.id = :companyId AND c.id = :couponId")
    Optional<Coupon> findCompanyCoupon(long companyId, long couponId);
    
    //------------------- COMPANY: find all coupons -----------------\\
    @Query   ("SELECT c FROM Coupon c WHERE c.company.id = :companyId")
    Optional<Collection<Coupon>> findAllCompanyCoupons(long companyId);
    
    //-------------------------- COMPANY: find all coupons by TYPE ------------------------------\\
    @Query("SELECT c FROM Coupon c WHERE c.company.id = :companyId AND c.couponType = :couponType")
    Optional<Collection<Coupon>> findAllCompanyCouponsByType(long companyId, CouponType couponType);
    
    //------------------------- COMPANY: find all coupons by PRICE ----------------------\\
    @Query ("SELECT c FROM Coupon c WHERE c.company.id = :companyId AND c.price <= :price")
    Optional<Collection<Coupon>> findAllCompanyCouponsByPrice(long companyId, double price);

    
//==================================================== CUSTOMER queries =======================================================\\
    
    
    //------------------------------------ CUSTOMER: find all coupon --------------------------------------------\\
    @Query("SELECT coupon FROM Customer c JOIN c.coupons coupon WHERE c.id = :customerId AND coupon.id = :couponId")
    Optional<Coupon> findCustomerCoupon(long customerId, long couponId);
    
    //----------------------------- CUSTOMER: find all coupons -------------------------\\
    @Query("SELECT coupon FROM Customer c JOIN c.coupons coupon WHERE c.id = :customerId")
    Optional<Collection<Coupon>> findAllCustomerCoupons(long customerId);
    
    //------------------------------------ CUSTOMER: find all coupons by TYPE ---------------------------------------------\\
    @Query("SELECT coupon FROM Customer c JOIN c.coupons coupon WHERE c.id = :customerId AND coupon.couponType = :couponType")
    Optional<Collection<Coupon>> findAllCustomerCouponsByType(long customerId, CouponType couponType);
    
    //---------------------------------------- CUSTOMER: find all coupons by PRICE --------------------------------\\
    @Query("SELECT coupon FROM Customer c JOIN c.coupons coupon WHERE c.id = :customerId AND coupon.price <= :price")
    Optional<Collection<Coupon>> findAllCustomerCouponsByPrice(long customerId, double price);

}
