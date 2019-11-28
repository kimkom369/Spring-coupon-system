package ghost_coupon.exceptionCoupon;

import ghost_coupon.exceptionMain.CouponSystemException;

public class CouponTitleDuplicateException extends CouponSystemException {
    private static final long serialVersionUID = 1L;

    public CouponTitleDuplicateException() {
    }

    public CouponTitleDuplicateException(String message) {
        super(message);
    }

    public CouponTitleDuplicateException(Throwable cause) {
        super(cause);
    }

    public CouponTitleDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponTitleDuplicateException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}