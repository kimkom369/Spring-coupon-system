package ghost_coupon.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ghost_coupon.entities.Company;
import ghost_coupon.entities.Coupon;
import ghost_coupon.entities.Income;
import ghost_coupon.enums.CouponType;
import ghost_coupon.enums.IncomeType;
import ghost_coupon.exceptionCompany.CompanyDoesntOwnCoupon;
import ghost_coupon.exceptionCoupon.CouponExpiredException;
import ghost_coupon.exceptionCoupon.CouponTitleDuplicateException;
import ghost_coupon.exceptionCoupon.CouponUnavaliableException;
import ghost_coupon.exceptionMain.CouponSystemException;
import ghost_coupon.exceptionMain.LoginFailedException;
import ghost_coupon.repositories.CompanyRepository;
import ghost_coupon.repositories.CouponRepository;
import ghost_coupon.repositories.IncomeRepository;
import ghost_coupon.utils.DateGenerator;
import ghost_coupon.utils.Validations;


/* 
 * @Autowired: Spring’s dependency injection wires an appropriate bean into the marked class member
 
 * @Service: Marks a Java class that performs some service, such as execute business logic.
 */



@Service
public class CompanyService implements Validations {

    private final CompanyRepository companyRepository;
    private final CouponRepository couponRepository;
    private final IncomeRepository incomeRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository,
                          CouponRepository couponRepository,
                          IncomeRepository incomeRepository) {
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
        this.incomeRepository = incomeRepository;
    }

//=================================== LOGIN method =================================================================\\
    
    
    
    public long login(String username,
                      String password) throws LoginFailedException {
        return companyRepository.findByNameAndPassword(username, password)
                .orElseThrow(() -> new LoginFailedException("Authorization is failed, please try again.")).getId();
    }

    
    
//=================================== COUPON methods =================================================================\\
    
    
    
    
    
    //------------------------- COUPON: company create coupon ------------------------------------------\\
    public void createCoupon(Company company, Coupon coupon) throws CouponTitleDuplicateException {
        this.isCouponTitleDuplicate(coupon.getTitle(), couponRepository);
        coupon.setCompany(company);
        couponRepository.save(coupon);

        incomeRepository.save(new Income(company,
                new Date(System.currentTimeMillis()),
                DateGenerator.getDateAfterMonths(6),
                IncomeType.COMPANY_CREATED_COUPON,100.0));
    }
    //------------------------- COUPON: company get coupon ----------------------------------------------\\
    public Coupon getCompanyCoupon(Company company, long couponId) throws CompanyDoesntOwnCoupon {
        return couponRepository.findCompanyCoupon(company.getId(), couponId)
                .orElseThrow(() -> new CompanyDoesntOwnCoupon("You don't own this coupon."));
    }
    //------------------------- COUPON: company get all coupons ------------------------------------------\\
    public Collection<Coupon> getAllCompanyCoupons(Company company) throws CouponSystemException {
        return couponRepository.findAllCompanyCoupons(company.getId())
                .orElseThrow(() -> new CompanyDoesntOwnCoupon("You have no coupons."));
    }
    //------------------------- COUPON: company get all coupons by TYPE ------------------------------------------------------\\
    public Collection<Coupon> getAllCompanyCouponsByType(Company company, CouponType couponType) throws CompanyDoesntOwnCoupon {
        return couponRepository.findAllCompanyCouponsByType(company.getId(), couponType)
                .orElseThrow(() -> new CompanyDoesntOwnCoupon("You have no coupons by type: " + couponType + "."));
    }
    //------------------------- COUPON: company get all coupons by PRICE ---------------------------------------------\\
    public Collection<Coupon> getAllCompanyCouponsByPrice(Company company, double price) throws CompanyDoesntOwnCoupon {
        return couponRepository.findAllCompanyCouponsByPrice(company.getId(), price)
                .orElseThrow(() -> new CompanyDoesntOwnCoupon("You have no coupons by price: " + price + "."));
    }
    //------------------------- COUPON: company update coupon ---------------------------------------------------------------------------------------------------------------\\
    public void updateCoupon(Company company, Coupon coupon) throws CouponUnavaliableException, CompanyDoesntOwnCoupon, CouponExpiredException, CouponTitleDuplicateException {

        this.isCompanyOwnsCoupon(company.getId(), coupon.getId(), couponRepository);
        this.isCouponTitleDuplicate(coupon.getTitle(), couponRepository);

        //--------------------- COUPON: Checks if updating end-date is not expired ---------------------------------------------------------\\
        if (coupon.getEndDate().after(new Date(System.currentTimeMillis()))) {

            if (coupon.getAmount() > 0) {

                if (coupon.getPrice() > 0) {

                    Coupon updCoupon = couponRepository.findById(coupon.getId()).get();
                    updCoupon.setEndDate(coupon.getEndDate());
                    updCoupon.setPrice(coupon.getPrice());
                    updCoupon.setAmount(coupon.getAmount());

                    couponRepository.save(updCoupon);

                    incomeRepository.save(new Income(company,
                            new Date(System.currentTimeMillis()),
                            DateGenerator.getDateAfterMonths(6),
                            IncomeType.COMPANY_UPDATED_COUPON,10.0)
                    		);
                } else {
                    throw new CouponUnavaliableException("Not allowed to update price to less than 1.");
                }
            } else {
                throw new CouponUnavaliableException("Not allowed to update amount to less than 1.");
            }
        } else {
            throw new CouponExpiredException("Not allowed to update the coupon to expired date.");
        }
    }
    //------------------------- COUPON: company delete --------------------------------------\\
    public void deleteCoupon(Company company, long couponId) throws CompanyDoesntOwnCoupon {
        this.isCompanyOwnsCoupon(company.getId(), couponId, couponRepository);
        couponRepository.deleteById(couponId);

        Income income = new Income(company,
                new Date(System.currentTimeMillis()),
                DateGenerator.getDateAfterMonths(6),
                IncomeType.COMPANY_DELETED_COUPON,
                10.0);
        
        incomeRepository.save(income);
    }

    
    
//================================================= INCOME method =======================================================================\\
    
    
    
    //------------------------------- INCOME: company get incomes ------------------------------------\\
    public Collection<Income> getCompanyIncomes(Company company) throws CouponSystemException {
        return incomeRepository.findCompanyIncomes(company.getId())
                .orElseThrow(() -> new CouponSystemException("There are no incomes of the companies."));
    }
}


