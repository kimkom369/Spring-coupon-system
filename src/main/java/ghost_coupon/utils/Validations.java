package ghost_coupon.utils;

import java.util.Optional;

import org.springframework.stereotype.Component;

import ghost_coupon.entities.Company;
import ghost_coupon.entities.Coupon;
import ghost_coupon.entities.Customer;
import ghost_coupon.exceptionCompany.CompanyDoesntOwnCoupon;
import ghost_coupon.exceptionCompany.CompanyEmailDuplicateException;
import ghost_coupon.exceptionCompany.CompanyNameDuplicateException;
import ghost_coupon.exceptionCompany.CompanyNotExistsException;
import ghost_coupon.exceptionCoupon.CouponNotExistsException;
import ghost_coupon.exceptionCoupon.CouponTitleDuplicateException;
import ghost_coupon.exceptionCustomer.CustomerAlreadyHasCouponException;
import ghost_coupon.exceptionCustomer.CustomerEmailDuplicateException;
import ghost_coupon.exceptionCustomer.CustomerNotExistsException;
import ghost_coupon.repositories.CompanyRepository;
import ghost_coupon.repositories.CouponRepository;
import ghost_coupon.repositories.CustomerRepository;

// this class is in charge to check the validation for each method.
@Component
public interface Validations {
    
	// COMPANY: does the company already exist
    default void isCompanyExists(long companyId, CompanyRepository companyRepository) throws CompanyNotExistsException {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotExistsException("This company doesn't exist."));
    }
    // COUPON: does the coupon already exist
    default void isCouponExists(long couponId, CouponRepository couponRepository) throws CouponNotExistsException {
        couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotExistsException("This coupon doesn't exist."));
    }
    // CUSTOMER: does the customer already exist
    default void isCustomerExists(long customerId, CustomerRepository customerRepository) throws CustomerNotExistsException {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistsException("This customer doesn't exist."));
    }
    // COMPANY: does the company name already exists
    default void isCompanyNameDuplicate(String companyName, CompanyRepository companyRepository) throws CompanyNameDuplicateException {
        Optional<Company> isCompanyNameDuplicate = companyRepository.findByName(companyName);
        if (isCompanyNameDuplicate.isPresent()) {
            throw new CompanyNameDuplicateException("Company name: " + companyName
                    + " already exists.");
        }
    }
    // COMPANY: does the company email already exist
    default void isCompanyEmailDuplicate(String companyEmail, CompanyRepository companyRepository) throws CompanyEmailDuplicateException {
        Optional<Company> isCompanyEmailDuplicate = companyRepository.findByEmail(companyEmail);
        if (isCompanyEmailDuplicate.isPresent()) {
            throw new CompanyEmailDuplicateException("Company email: " + companyEmail
                    + " already exists.");
        }
    }
    // CUSTOMER: does the customers email already exist
    default void isCustomerEmailDuplicate(String customerEmail, CustomerRepository customerRepository) throws CustomerEmailDuplicateException {
        Optional<Customer> isCustomerEmailDuplicate = customerRepository.findByEmail(customerEmail);
        if (isCustomerEmailDuplicate.isPresent()) {
            throw new CustomerEmailDuplicateException("Customer email: " + customerEmail
                    + " already exists.");
        }
    }
    // COUPON: does the title of the coupon already exist
    default void isCouponTitleDuplicate(String couponTitle, CouponRepository couponRepository) throws CouponTitleDuplicateException {
        Optional<Coupon> isCouponTitleDuplicate = couponRepository.findByTitle(couponTitle);
        if (isCouponTitleDuplicate.isPresent()) {
            throw new CouponTitleDuplicateException("Coupon title: " + couponTitle
                    + " already exists.");
        }
    }
    // COMPANY: does the company already own the coupon
    default void isCompanyOwnsCoupon(long companyId, long couponId, CouponRepository couponRepository) throws CompanyDoesntOwnCoupon {
        couponRepository.findCompanyCoupon(companyId, couponId)
                .orElseThrow(() -> new CompanyDoesntOwnCoupon("You don't own this coupon."));
    }
    // CUSTOMER: does the customer already own the coupon
    default void isCustomerHasCoupon(long customerId, long couponId, CouponRepository couponRepository) throws CustomerAlreadyHasCouponException {
        Optional<Coupon> isCustomerHasCoupon = couponRepository.findCustomerCoupon(customerId, couponId);
        if (isCustomerHasCoupon.isPresent()) {
            throw new CustomerAlreadyHasCouponException("You already have this coupon.");
        }
    }
}
