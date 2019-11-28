package ghost_coupon.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ghost_coupon.repositories.CouponRepository;
import ghost_coupon.repositories.IncomeRepository;
import ghost_coupon.repositories.TokenRepository;

/*
 * Daily Expiration Task ==> Thread
 * 
 * runs the whole systems thread
 *  
 */

@Component
public class DailyExpirationTask extends Thread {

    private final CouponRepository couponRepository;
    private final TokenRepository tokenRepository;
    private final IncomeRepository incomeRepository;

    @Autowired
    public DailyExpirationTask(CouponRepository couponRepository,
                            TokenRepository tokenRepository,
                            IncomeRepository incomeRepository) {
        this.couponRepository = couponRepository;
        this.tokenRepository = tokenRepository;
        this.incomeRepository = incomeRepository;
    }

    @Override
    public void run() {
        while (true) {
            try {
                couponRepository.deleteExpiredCoupons();
                tokenRepository.deleteExpiredTokens();
                incomeRepository.deleteExpiredIncomes();
                TimeUnit.DAYS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
