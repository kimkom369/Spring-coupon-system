package ghost_coupon.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ghost_coupon.entities.Company;
import ghost_coupon.entities.Coupon;
import ghost_coupon.entities.Customer;
import ghost_coupon.entities.Income;
import ghost_coupon.enums.ClientType;
import ghost_coupon.enums.IncomeType;
import ghost_coupon.exceptionCompany.CompanyEmailDuplicateException;
import ghost_coupon.exceptionCompany.CompanyNameDuplicateException;
import ghost_coupon.exceptionCompany.CompanyNotExistsException;
import ghost_coupon.exceptionCoupon.CouponExpiredException;
import ghost_coupon.exceptionCoupon.CouponNotExistsException;
import ghost_coupon.exceptionCoupon.CouponTitleDuplicateException;
import ghost_coupon.exceptionCustomer.CustomerEmailDuplicateException;
import ghost_coupon.exceptionCustomer.CustomerNotExistsException;
import ghost_coupon.exceptionMain.CouponSystemException;
import ghost_coupon.exceptionMain.LoginFailedException;
import ghost_coupon.repositories.CompanyRepository;
import ghost_coupon.repositories.CouponRepository;
import ghost_coupon.repositories.CustomerRepository;
import ghost_coupon.repositories.IncomeRepository;
import ghost_coupon.repositories.TokenRepository;
import ghost_coupon.repositories.UserRepository;
import ghost_coupon.utils.DateGenerator;
import ghost_coupon.utils.Validations;

/* 
 * @Autowired: Spring’s dependency injection wires an appropriate bean into the marked class member
 
 * @Service: Specialization of the @Component,has no encapsulated state.
 */

@Service
public class AdminService implements Validations {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;
    private final IncomeRepository incomeRepository;
    private final TokenRepository tokenRepository;

    
    @Autowired
    public AdminService(UserRepository userRepository,
                        CompanyRepository companyRepository,
                        CouponRepository couponRepository,
                        CustomerRepository customerRepository,
                        IncomeRepository incomeRepository,
                        TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
        this.customerRepository = customerRepository;
        this.incomeRepository = incomeRepository;
        this.tokenRepository = tokenRepository;
    }
//======================================= LOGIN method =====================================================================\\
    
    public long login(String username,String password) throws LoginFailedException {
    	if ((username=="admin")&&(password=="admin"))
        return userRepository.login(username, password)
                .orElseThrow(() -> new LoginFailedException("Authorization is failed, please try again.")).getId();
		return 0;
    }

//======================================= COMPANY methods ===================================================================\\
    
  
    
    //-------------------------- COMPANY: admin create company -------------------------------------------\\
    public void createCompany(Company company) throws CompanyNameDuplicateException, CompanyEmailDuplicateException {
        this.isCompanyNameDuplicate(company.getName(), companyRepository);
        this.isCompanyEmailDuplicate(company.getEmail(), companyRepository);
        companyRepository.save(company);
    }
    //-------------------------- COMAPNY: admin get company by ID -----------------------------\\
    public Company getCompanyById(long companyId) throws CompanyNotExistsException {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotExistsException("This company doesn't exist."));
    }
    //-------------------------- COMPANY: admin get all companies -------------------------\\
    public Collection<Company> getAllCompanies() throws CompanyNotExistsException {
        return companyRepository.findAllCompanies()
                .orElseThrow(() -> new CompanyNotExistsException("There are no companies."));
    }
    //-------------------------- COMPANY: admin update company ----------------------------------------------------------\\
    public void updateCompany(Company company) throws CompanyNameDuplicateException, CompanyNotExistsException, CompanyEmailDuplicateException {
        this.isCompanyExists(company.getId(), companyRepository);
        this.isCompanyNameDuplicate(company.getName(), companyRepository);
        this.isCompanyEmailDuplicate(company.getEmail(), companyRepository);
        companyRepository.save(company);
    }
    //-------------------------- COMPANY: admin delete company --------------------\\
    public void deleteCompany(long companyId) {
        companyRepository.deleteById(companyId);
        tokenRepository.deleteAllByClientTypeAndUserId(ClientType.COMPANY, companyId);
    }

    
//====================================== COUPON methods ========================================================================\\
    
   
    
    //--------------------------- COUPON: admin create coupon -------------------------------------------------------------\\
    public void createCoupon(long companyId, Coupon coupon) throws CompanyNotExistsException, CouponTitleDuplicateException {
        this.isCouponTitleDuplicate(coupon.getTitle(), couponRepository);
        coupon.setCompany(companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotExistsException("This company doesn't exist.")));
        couponRepository.save(coupon);

        incomeRepository.save(new Income(
                new Date(System.currentTimeMillis()),
                DateGenerator.getDateAfterMonths(6),
                IncomeType.ADMIN_CREATED_COUPON));
    }
    
    //--------------------------- COUPON: admin get coupon by ID ----------------------------\\
    public Coupon getCouponById(long couponId) throws CouponSystemException {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotExistsException("This coupon doesn't exist."));
    }
    //--------------------------- COUPON: admin get all coupons -----------------------------\\
    public Collection<Coupon> getAllCoupons() throws CouponNotExistsException {
        return couponRepository.findAllCoupons()
                .orElseThrow(() -> new CouponNotExistsException("There are no coupons."));
    }
    //--------------------------- COUPON: admin update coupon --------------------------------\\
    public void updateCoupon(long companyId, Coupon coupon) throws CouponSystemException {

        this.isCouponExists(coupon.getId(), couponRepository);
        this.isCouponTitleDuplicate(coupon.getTitle(), couponRepository);
        coupon.setCompany(companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotExistsException("This company doesn't exist.")));

        if (coupon.getEndDate().after(new Date(System.currentTimeMillis()))) {

            if (coupon.getAmount() > 0) {

                if (coupon.getPrice() > 0) {

                    couponRepository.save(coupon);

                    incomeRepository.save(new Income(
                            new Date(System.currentTimeMillis()),
                            DateGenerator.getDateAfterMonths(6),
                            IncomeType.ADMIN_UPDATED_COUPON));
                } else {
                    throw new CouponExpiredException("Not allowed to update price to less than 1.");
                }
            } else {
                throw new CouponExpiredException("Not allowed to update amount to less than 1.");
            }
        } else {
            throw new CouponExpiredException("Not allowed to update the coupon to expired date.");
        }
    }
    //-------------- COUPON: admin delete coupon ------------------\\
    public void deleteCoupon(long couponId) {
        couponRepository.deleteById(couponId);

        incomeRepository.save(new Income(
                new Date(System.currentTimeMillis()),
                DateGenerator.getDateAfterMonths(6),
                IncomeType.ADMIN_DELETED_COUPON));
    }

    
    
//==================================== CUSTOMER methods =============================================================\\
    
    
    
    //------------------------- CUSTOMER: admin create customer -----------------------------------\\
    public void createCustomer(Customer customer) throws CustomerEmailDuplicateException {
        this.isCustomerEmailDuplicate(customer.getEmail(), customerRepository);
        customerRepository.save(customer);
    }
    //------------------------- CUSTOMER: admin get customer by ID --------------------------------\\
    public Customer getCustomerById(long customerId) throws CustomerNotExistsException {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistsException("This customer doesn't exist."));
    }
    //------------------------- CUSTOMER: admin get all customers ---------------------------------\\
    public Collection<Customer> getAllCustomers() throws CustomerNotExistsException {
        return customerRepository.findAllCustomers()
                .orElseThrow(() -> new CustomerNotExistsException("There are no customers."));
    }
    //------------------------- CUSTOMER: admin update customer ----------------------------------------------------\\
    public void updateCustomer(Customer customer) throws CustomerNotExistsException, CustomerEmailDuplicateException {
        this.isCustomerExists(customer.getId(), customerRepository);
        this.isCustomerEmailDuplicate(customer.getEmail(), customerRepository);
        customerRepository.save(customer);
    }
    //------------------------- CUSTOMER: admin delete customer ----------------------\\
    public void deleteCustomer(long customerId) {
        customerRepository.deleteById(customerId);
        tokenRepository.deleteAllByClientTypeAndUserId(ClientType.CUSTOMER, customerId);
    }

    
//====================================== INCOME methods ========================================================================\\
    
    
    
    //--------------------------- INCOME: admin get all incomes -----------------------------------\\
    public Collection<Income> getAllIncomes() throws CouponSystemException {
        return incomeRepository.findAllIncomes()
                .orElseThrow(() -> new CouponSystemException("There are no incomes."));
    }
    //--------------------------- INCOME: admin get company incomes --------------------------------\\
    public Collection<Income> getCompanyIncomes(long companyId) throws CouponSystemException {
        return incomeRepository.findCompanyIncomes(companyId)
                .orElseThrow(() -> new CouponSystemException("There are no incomes of the companies."));
    }
    //--------------------------- INCOME: admin get customer incomes --------------------------------\\
    public Collection<Income> getCustomerIncomes(long customerId) throws CouponSystemException {
        return incomeRepository.findCustomerIncomes(customerId)
                .orElseThrow(() -> new CouponSystemException("There are no incomes of the customers."));
    }

}
