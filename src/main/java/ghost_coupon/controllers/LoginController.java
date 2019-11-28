package ghost_coupon.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ghost_coupon.entities.Token;
import ghost_coupon.enums.ClientType;
import ghost_coupon.exceptionMain.LoginFailedException;
import ghost_coupon.mainSystem.CouponSystem;
import ghost_coupon.repositories.TokenRepository;
import ghost_coupon.utils.DateGenerator;
import ghost_coupon.utils.SecureTokenGenerator;


/*Annotations:

 * @Controller - marks the class as web controller, capable of handling the requests.
   
 * @RestController  - a convenience annotation of a @Controller and @ResponseBody. 
    
 */



@RestController
@RequestMapping("login")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController {

    private final CouponSystem couponSystem;
    private final TokenRepository tokenRepository;

    @Autowired
    public LoginController(CouponSystem couponSystem, TokenRepository tokenRepository) {
        this.couponSystem = couponSystem;
        this.tokenRepository = tokenRepository;
    }
    
//============================================================ LOGIN methods ================================================================================\\

    //-------------------------------------------------- ADMIN,COMPANY,CUSTOMER ------------------------------------------------------------\\
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        switch (user.getClientType()) {
            
            case ADMIN:
                try { 
                    long adminId = couponSystem.login(user.getName(), user.getPassword(), user.getClientType());
                    Token token = new Token(adminId, ClientType.ADMIN, DateGenerator.getDateAfterMonths(2), SecureTokenGenerator.nextToken());
                    return getResponseEntity(response, token);
                } catch (LoginFailedException e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            case COMPANY:
                try {
                    long companyId = couponSystem.login(user.getName(), user.getPassword(), user.getClientType());
                    Token token = new Token(companyId, ClientType.COMPANY, DateGenerator.getDateAfterMonths(2), SecureTokenGenerator.nextToken());
                    return getResponseEntity(response, token);
                } catch (LoginFailedException e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            case CUSTOMER:
                try {
                    long customerId = couponSystem.login(user.getName(), user.getPassword(), user.getClientType());
                    Token token = new Token(customerId, ClientType.CUSTOMER, DateGenerator.getDateAfterMonths(2), SecureTokenGenerator.nextToken());
                    return getResponseEntity(response, token);
                } catch (LoginFailedException e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    private ResponseEntity<?> getResponseEntity(HttpServletResponse response, Token token) {
        tokenRepository.save(token);
        Cookie cookie = new Cookie("auth", token.getToken());
        cookie.setMaxAge(/* ~2 months */ 5000000);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
//-------- USER: entity -----------\\
class User {
    private String name;
    private String password;
    private ClientType clientType;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public ClientType getClientType() {
        return clientType;
    }
}
