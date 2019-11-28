package ghost_coupon.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ghost_coupon.entities.Coupon;
import ghost_coupon.entities.Customer;
import ghost_coupon.entities.Income;
import ghost_coupon.enums.CouponType;
import ghost_coupon.enums.IncomeType;
import ghost_coupon.exceptionCoupon.CouponExpiredException;
import ghost_coupon.exceptionCoupon.CouponNotExistsException;
import ghost_coupon.exceptionCoupon.CouponUnavaliableException;
import ghost_coupon.exceptionCustomer.CustomerDoesntOwnCoupon;
import ghost_coupon.exceptionMain.CouponSystemException;
import ghost_coupon.exceptionMain.LoginFailedException;
import ghost_coupon.repositories.CouponRepository;
import ghost_coupon.repositories.CustomerRepository;
import ghost_coupon.repositories.IncomeRepository;
import ghost_coupon.utils.DateGenerator;
import ghost_coupon.utils.Validations;


/*
 * @Autowired - Spring’s dependency injection wires an appropriate bean into the marked class member
 * @Service   - Specialization of the @Component,has no encapsulated state.
 */


@Service
public class CustomerService implements Validations {

    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final IncomeRepository incomeRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           CouponRepository couponRepository,
                           IncomeRepository incomeRepository) {
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
        this.incomeRepository = incomeRepository;
    }

//================================== LOGIN method ================================================================\\
    
    
    public long login(String username,
                      String password) throws LoginFailedException {
        return customerRepository.findByNameAndPassword(username, password)
                .orElseThrow(() -> new LoginFailedException("Authorization is failed, please try again.")).getId();
    }


//================================== CUSTOMER methods ===============================================================\\
    
    
    
    
    //---------------------- CUSTOMER: customer purchase coupon -----------------------------\\
    public void purchaseCoupon(Customer customer, long couponId) throws CouponSystemException {

        
    	//------------------ CUSTOMER: checks if customer already owns coupon ----------------\\
        this.isCustomerHasCoupon(customer.getId(), couponId, couponRepository);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotExistsException("This coupon doesn't exist."));

        
        //------------------ CUSTOMER: checks if coupons isn't expired -----------------------\\
        if (coupon.getEndDate().after(new Date(System.currentTimeMillis()))) {

            //-------------- CUSTOMER: Checks coupon amounts ----------------------------------\\
            if (coupon.getAmount() > 0) {
                
                coupon.setAmount(coupon.getAmount() - 1);
                couponRepository.save(coupon);

                customer.purchaseCoupon(coupon);
                customerRepository.save(customer);

                Income income = new Income(customer,coupon.getPrice(),
                        new Date(System.currentTimeMillis()),
                        DateGenerator.getDateAfterMonths(6),
                        IncomeType.CUSTOMER_PURCHASED_COUPON
                        );
                incomeRepository.save(income);
            } else {
                throw new CouponUnavaliableException("This coupon is not available");
            }
        } else {
            throw new CouponExpiredException("This coupon is expired.");
        }
    }
    //-------------------- CUSTOMER: customer get purchased coupon --------------------------------\\
    public Collection<Coupon> getPurchasedCoupons(Customer customer) throws CustomerDoesntOwnCoupon {
        return couponRepository.findAllCustomerCoupons(customer.getId())
                .orElseThrow(() -> new CustomerDoesntOwnCoupon("You have no coupons."));
    }
    //-------------------- CUSTOMER: customer get purchase coupon by TYPE ------------------------------------------------------\\
    public Collection<Coupon> getPurchasedCouponsByType(Customer customer, CouponType couponType) throws CustomerDoesntOwnCoupon {
        return couponRepository.findAllCustomerCouponsByType(customer.getId(), couponType)
                .orElseThrow(() -> new CustomerDoesntOwnCoupon("You have no coupons by type: " + couponType + "."));
    }
    //-------------------- CUSTOMER: customer get purchase coupon by PRICE ---------------------------------------------\\
    public Collection<Coupon> getPurchasedCouponsByPrice(Customer customer, double price) throws CustomerDoesntOwnCoupon {
        return couponRepository.findAllCustomerCouponsByPrice(customer.getId(), price)
                .orElseThrow(() -> new CustomerDoesntOwnCoupon("You have no coupons by type: " + price + "."));
    }
    //-------------------- CUSTOMER: customer get all available coupons ----------------\\
    public Collection<Coupon> getAllAvailableCoupons() throws CouponUnavaliableException {
        return couponRepository.findAllAvailableCoupons()
                .orElseThrow(() -> new CouponUnavaliableException("There are no available coupons."));
    }

    

//================================== INCOME method =============================================================================\\
    
    
    //-------------------- INCOME: customer get all incomes ------------------------------------------\\
    public Collection<Income> getCustomerIncomes(long customerId) throws CouponSystemException {
        return incomeRepository.findCustomerIncomes(customerId)
                .orElseThrow(() -> new CouponSystemException("There are no incomes of the customers."));
    }
}
