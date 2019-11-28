package ghost_coupon.mainSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ghost_coupon.enums.ClientType;
import ghost_coupon.exceptionMain.LoginFailedException;
import ghost_coupon.services.AdminService;
import ghost_coupon.services.CompanyService;
import ghost_coupon.services.CustomerService;
import ghost_coupon.utils.DailyExpirationTask;


@Component
public class CouponSystem {

    private final AdminService adminService;
    private final CompanyService companyService;
    private final CustomerService customerService;
    private final DailyExpirationTask task;
    private boolean firstCleaning = true;

    @Autowired
    public CouponSystem(AdminService adminService,
                        CompanyService companyService,
                        CustomerService customerService,
                        DailyExpirationTask task) {
        this.adminService = adminService;
        this.companyService = companyService;
        this.customerService = customerService;
        this.task = task;
    }

    public long login(String username , String password , ClientType clientType) throws LoginFailedException {
        if (firstCleaning) task.start();
        firstCleaning = false;

        switch (clientType) {
            case ADMIN:
            	if(username.equals("admin") && password.equals("admin"))
                return adminService.login(username, password);
            case COMPANY:
                return companyService.login(username, password);
            case CUSTOMER:
                return customerService.login(username, password);
            default:
                throw new LoginFailedException("Authorization has failed, please try again.");
        }
    }
}