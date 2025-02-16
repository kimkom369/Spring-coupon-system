package ghost_coupon.exceptionCustomer;

import ghost_coupon.exceptionMain.CouponSystemException;

public class CustomerEmailDuplicateException extends CouponSystemException {
    private static final long serialVersionUID = 1L;

    public CustomerEmailDuplicateException() {
    }

    public CustomerEmailDuplicateException(String message) {
        super(message);
    }

    public CustomerEmailDuplicateException(Throwable cause) {
        super(cause);
    }

    public CustomerEmailDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerEmailDuplicateException(String message, Throwable cause, boolean enableSuppression,
                                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
