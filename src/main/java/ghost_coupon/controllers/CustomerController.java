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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ghost_coupon.entities.Coupon;
import ghost_coupon.entities.Customer;
import ghost_coupon.entities.Token;
import ghost_coupon.enums.ClientType;
import ghost_coupon.enums.CouponType;
import ghost_coupon.exceptionCoupon.CouponUnavaliableException;
import ghost_coupon.exceptionMain.CouponSystemException;
import ghost_coupon.exceptionMain.LoginFailedException;
import ghost_coupon.repositories.CustomerRepository;
import ghost_coupon.repositories.TokenRepository;
import ghost_coupon.services.CustomerService;


/*Annotations:

 * @Controller - marks the class as web controller, capable of handling the requests.
   
 * @RestController  - a convenience annotation of a @Controller and @ResponseBody. 
    
 */


@RestController
@RequestMapping(path = "customer")
@Scope("request")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;
    private final HttpServletRequest request;

    @Autowired
    public CustomerController(CustomerService customerService,
                              CustomerRepository customerRepository,
                              TokenRepository tokenRepository,
                              HttpServletRequest request) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.tokenRepository = tokenRepository;
        this.request = request;
    }

//====================================== COUPON methods ===============================================\\
   
    //--------------------- GET: customer purchase coupon --------------------\\
    @RequestMapping(path = "coupons/{couponId}", method = RequestMethod.GET)
    public ResponseEntity<?> purchaseCoupon(@PathVariable long couponId) {
        try {
            customerService.purchaseCoupon(getCustomer(), couponId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //-------------------------- GET: customer get all coupons ---------------------------\\
    @RequestMapping(path = "coupons", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCustomerCoupons() {
        try {
            Collection<Coupon> coupons = customerService.getPurchasedCoupons(getCustomer());
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //--------------------------- GET: customer purchase coupon by TYPE ------------------------------------\\
    @RequestMapping(path = "coupons-by-type/{couponType}", method = RequestMethod.GET)
    public ResponseEntity<?> getPurchasedCouponsByType(@PathVariable CouponType couponType) {
        try {
            Collection<Coupon> coupons = customerService.getPurchasedCouponsByType(getCustomer(), couponType);
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------------------- GET: customer purchase coupon by PRICE ----------------------------------\\ 
    @RequestMapping(path = "coupons-by-price/{price}", method = RequestMethod.GET)
    public ResponseEntity<?> getPurchasedCouponsByPrice(@PathVariable double price) {
        try {
            Collection<Coupon> coupons = customerService.getPurchasedCouponsByPrice(getCustomer(), price);
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //------------------ GET: customer get all available coupons ---------------\\
    @RequestMapping(path = "coupons-available", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAvailableCoupons() {
        try {
            Collection<Coupon> coupons = customerService.getAllAvailableCoupons();
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (CouponUnavaliableException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    private Customer getCustomer() throws LoginFailedException {
        Token token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("auth")) {
                token = tokenRepository.findByClientTypeAndToken(ClientType.CUSTOMER, c.getValue())
                        .orElseThrow(() -> new LoginFailedException("Authorization is failed, please try again."));
            }
        }
        return customerRepository.findById(token.getUserId())
                .orElseThrow(() -> new LoginFailedException("Authorization is failed, please try again."));
    }
}
