package ghost_coupon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ghost_coupon.entities.Company;
import ghost_coupon.entities.Customer;
import ghost_coupon.exceptionMain.CouponSystemException;
import ghost_coupon.services.RegistrationService;


/*Annotations:
  
 * @Controller - marks the class as web controller, capable of handling the requests.
   
 * @RestController  - a convenience annotation of a @Controller and @ResponseBody. 
    
 */


@RestController
@RequestMapping("registration")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class RegistrationController {

    private final RegistrationService registration;

    @Autowired
    public RegistrationController(RegistrationService registration) {
        this.registration = registration;
    }
//================================= REGISTER methods =====================================\\
   
    //---------------------- POST: register company --------------------------\\
    @RequestMapping(path = "companies", method = RequestMethod.POST)
    public ResponseEntity<?> registerCompany(@RequestBody Company company) {
        try {
            registration.registerCompany(company);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //---------------------- POST: register customer --------------------------\\
    @RequestMapping(path = "customers", method = RequestMethod.POST)
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        try {
            registration.registerCustomer(customer);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CouponSystemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}