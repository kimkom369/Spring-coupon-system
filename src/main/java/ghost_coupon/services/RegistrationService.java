package ghost_coupon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ghost_coupon.entities.Company;
import ghost_coupon.entities.Customer;
import ghost_coupon.exceptionMain.CouponSystemException;


/*
 * @Autowired - Spring’s dependency injection wires an appropriate bean into the marked class member
 * @Service   - Specialization of the @Component,has no encapsulated state.
 */

@Service
public class RegistrationService {

    private final AdminService adminService;

    //------------- REGISTRATION: admin creating COMPANY || CUSTOMER ----------------\\
    @Autowired
    public RegistrationService(AdminService adminService) {
        this.adminService = adminService;
    }

    public void registerCompany(Company company) throws CouponSystemException {
        adminService.createCompany(company);
    }

    public void registerCustomer(Customer customer) throws CouponSystemException {
        adminService.createCustomer(customer);
    }
}