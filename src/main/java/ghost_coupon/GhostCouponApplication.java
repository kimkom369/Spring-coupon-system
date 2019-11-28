package ghost_coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
@ServletComponentScan
public class GhostCouponApplication {

    public static void main(String[] args) {
        // main class which loads spring boot App
        SpringApplication.run(GhostCouponApplication.class, args);
        System.out.println();
        System.out.println("Project Is Initiated");
    }
}