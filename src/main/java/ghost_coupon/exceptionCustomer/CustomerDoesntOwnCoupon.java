package ghost_coupon.exceptionCustomer;

import ghost_coupon.exceptionMain.CouponSystemException;

public class CustomerDoesntOwnCoupon extends CouponSystemException {
    private static final long serialVersionUID = 1L;

    public CustomerDoesntOwnCoupon() {
    }

    public CustomerDoesntOwnCoupon(String message) {
        super(message);
    }

    public CustomerDoesntOwnCoupon(Throwable cause) {
        super(cause);
    }

    public CustomerDoesntOwnCoupon(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerDoesntOwnCoupon(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
