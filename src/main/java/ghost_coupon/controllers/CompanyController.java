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
import ghost_coupon.entities.Income;
import ghost_coupon.entities.Token;
import ghost_coupon.enums.ClientType;
import ghost_coupon.enums.CouponType;
import ghost_coupon.exceptionMain.CouponSystemException;
import ghost_coupon.exceptionMain.LoginFailedException;
import ghost_coupon.repositories.CompanyRepository;
import ghost_coupon.repositories.TokenRepository;
import ghost_coupon.services.CompanyService;


/*Annotations:

 * @Controller - marks the class as web controller, capable of handling the requests.
   
 * @RestController  - a convenience annotation of a @Controller and @ResponseBody. 
    
 */


@RestController
@RequestMapping(path = "company")
@Scope("request")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final TokenRepository tokenRepository;
    private final HttpServletRequest request;

    @Autowired
    public CompanyController(CompanyService companyService,
                             CompanyRepository companyRepository,
                             TokenRepository tokenRepository,
                             HttpServletRequest request) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.tokenRepository = tokenRepository;
        this.request = request;
    }

//================================== COUPON methods ==========================================\\
    
    //--------------------- POST: company create coupon -----------------------\\
    @RequestMapping(path = "coupons", method = RequestMethod.POST)
    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon) {
        try {
            companyService.createCoupon(getCompany(), coupon);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //--------------------- GET: company get coupon -------------------------------\\
    @RequestMapping(path = "coupons/{couponId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCompanyCoupon(@PathVariable long couponId) {
        try {
            Coupon coupon = companyService.getCompanyCoupon(getCompany(), couponId);
            return new ResponseEntity<>(coupon, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //---------------------------- GET: company get all coupons -------------------------\\
    @RequestMapping(path = "coupons", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCompanyCoupons() {
        try {
            Collection<Coupon> coupons = companyService.getAllCompanyCoupons(getCompany());
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------------------ GET: company get coupon by TYPE --------------------------------------\\
    @RequestMapping(path = "coupons-by-type/{couponType}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCompanyCouponsByType(@PathVariable CouponType couponType) {
        try {
            Collection<Coupon> coupons = companyService.getAllCompanyCouponsByType(getCompany(), couponType);
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------------------ GET: company get coupon by PRICE -----------------------------------\\
    @RequestMapping(path = "coupons-by-price/{price}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCompanyCouponsByPrice(@PathVariable double price) {
        try {
            Collection<Coupon> coupons = companyService.getAllCompanyCouponsByPrice(getCompany(), price);
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //---------------------- PUT: company update coupon ----------------------\\
    @RequestMapping(path = "coupons", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon) {
        try {
            companyService.updateCoupon(getCompany(), coupon);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //--------------------- DELETE: company delete coupon --------------------\\
    @RequestMapping(path = "coupons/{couponId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCoupon(@PathVariable long couponId) {
        try {
            companyService.deleteCoupon(getCompany(), couponId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
//========================================== INCOME method ============================================\\
    
    //--------------------------- GET: company get income -----------------------------\\
    @RequestMapping(path = "incomes", method = RequestMethod.GET)
    public ResponseEntity<?> getCompanyIncomes() {
        try {
            Collection<Income> incomes = companyService.getCompanyIncomes(getCompany());
            return new ResponseEntity<>(incomes, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    private Company getCompany() throws LoginFailedException {
        Token token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("auth")) {
                token = tokenRepository.findByClientTypeAndToken(ClientType.COMPANY, c.getValue())
                        .orElseThrow(() -> new LoginFailedException("Authorization is failed, please try again."));
            }
        }
        return companyRepository.findById(token.getUserId())
                .orElseThrow(() -> new LoginFailedException("Authorization is failed, please try again."));
    }
}