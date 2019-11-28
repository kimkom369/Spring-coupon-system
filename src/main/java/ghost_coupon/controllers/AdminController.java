package ghost_coupon.controllers;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ghost_coupon.entities.Company;
import ghost_coupon.entities.Coupon;
import ghost_coupon.entities.Customer;
import ghost_coupon.entities.Income;
import ghost_coupon.enums.ClientType;
import ghost_coupon.exceptionMain.CouponSystemException;
import ghost_coupon.exceptionMain.LoginFailedException;
import ghost_coupon.repositories.TokenRepository;
import ghost_coupon.services.AdminService;


/*Annotations:

 * @Controller - marks the class as web controller, capable of handling the requests.
   
 * @RestController  - a convenience annotation of a @Controller and @ResponseBody. 
    
 */

@RestController
@RequestMapping(path = "admin")
@Scope("request")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdminController {

    private final AdminService adminService;
    private final TokenRepository tokenRepository;
    private final HttpServletRequest request;

    @Autowired
    public AdminController(AdminService adminService,TokenRepository tokenRepository,HttpServletRequest request) {
        this.adminService = adminService;
        this.tokenRepository = tokenRepository;
        this.request = request;
    }

    
//==================================== COMPANY 	methods ========================================\\
   
    //-------------------- POST: admin create company -------------------\\
    @RequestMapping(path = "companies", method = RequestMethod.POST) 
    public ResponseEntity<?> createCompany(@RequestBody Company company) {
        try {
            cookieCheck();
            adminService.createCompany(company);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
        }
    }
    //--------------------- GET: admin get company --------------------------\\
    @RequestMapping(path = "companies/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCompanyById(@PathVariable long companyId) {
        try {
            cookieCheck();
            Company company = adminService.getCompanyById(companyId);
            return new ResponseEntity<>(company, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //----------------------- GET: admin get all companies -------------------\\
    @RequestMapping(path = "companies", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCompanies() {
        try {
            cookieCheck();
            Collection<Company> companies = adminService.getAllCompanies();
            return new ResponseEntity<>(companies, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //-------------------- PUT: admin update company -------------------------\\
    @RequestMapping(path = "companies", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCompany(@RequestBody Company company) {
        try {
            cookieCheck();
            adminService.updateCompany(company);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //-------------------- DELETE: admin delete company -------------------------\\
    @RequestMapping(path = "companies/{companyId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCompany(@PathVariable long companyId) {
        try {
            cookieCheck();
            adminService.deleteCompany(companyId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//============================================ COUPON methods ==================================================\\
    
    //------------------------------ POST: admin create coupon ------------------------------------\\
    @RequestMapping(path = "coupons/{companyId}", method = RequestMethod.POST)
    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon, @PathVariable long companyId) {
        try {
            cookieCheck();
            adminService.createCoupon(companyId, coupon);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------------- GET: admin get coupon -------------------------\\
    @RequestMapping(path = "coupons/{couponId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCouponById(@PathVariable long couponId) {
        try {
            cookieCheck();
            Coupon coupon = adminService.getCouponById(couponId);
            return new ResponseEntity<>(coupon, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------------ GET: admin get all coupon ----------------------\\
    @RequestMapping(path = "coupons", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCoupons() {
        try {
            cookieCheck();
            Collection<Coupon> coupons = adminService.getAllCoupons();
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------------------- PUT: admin update coupon ------------------------------------\\
    @RequestMapping(path = "coupons/{companyId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon, @PathVariable long companyId) {
        try {
            cookieCheck();
            adminService.updateCoupon(companyId, coupon);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //---------------------- DELETE: admin delete coupon --------------------\\
    @RequestMapping(path = "coupons/{couponId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCoupon(@PathVariable long couponId) {
        try {
            cookieCheck();
            adminService.deleteCoupon(couponId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//============================================== CUSTOMER methods ===================================================\\
    
    //---------------------- POST: admin create customer -------------------\\
    @RequestMapping(path = "customers", method = RequestMethod.POST)
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            cookieCheck();
            adminService.createCustomer(customer);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //---------------------- GET: admin get customers ------------------------\\
    @RequestMapping(path = "customers/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerById(@PathVariable long customerId) {
        try {
            cookieCheck();
            Customer customer = adminService.getCustomerById(customerId);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //----------------------- GET: admin get all customers -----------------\\
    @RequestMapping(path = "customers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCustomers() {
        try {
            cookieCheck();
            Collection<Customer> customers = adminService.getAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------------ PUT: admin update customer ------------------\\
    @RequestMapping(path = "customers", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try {
            cookieCheck();
            adminService.updateCustomer(customer);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //---------------------- DELETE: admin delete customers ---------------------\\
    @RequestMapping(path = "customers/{customerId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCustomer(@PathVariable long customerId) {
        try {
            cookieCheck();
            adminService.deleteCustomer(customerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//=========================================== INCOME methods ==================================================\\
    
    //---------------------- GET: admin get all incomes -----------------------\\
    @RequestMapping(path = "incomes", method = RequestMethod.GET)
    public ResponseEntity<?> getAllIncomes() {
        try {
            cookieCheck();
            Collection<Income> incomes = adminService.getAllIncomes();
            return new ResponseEntity<>(incomes, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //----------------------- GET: admin get company income -----------------------\\
    @RequestMapping(path = "company-incomes/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCompanyIncomes(@PathVariable long companyId) {
        try {
            cookieCheck();
            Collection<Income> incomes = adminService.getCompanyIncomes(companyId);
            return new ResponseEntity<>(incomes, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------------ GET: admin get customer income ------------------------\\
    @RequestMapping(path = "customer-incomes/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerIncomes(@PathVariable long customerId) {
        try {
            cookieCheck();
            Collection<Income> incomes = adminService.getCustomerIncomes(customerId);
            return new ResponseEntity<>(incomes, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    private void cookieCheck() throws LoginFailedException {
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("auth")) {
                if (!tokenRepository.findByClientTypeAndToken(ClientType.ADMIN, c.getValue()).isPresent()) {
                    throw new LoginFailedException("Authorization is failed, please try again.");
                }
            }
        }
    }
}