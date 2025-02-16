package ghost_coupon.exceptionCustomer;

import ghost_coupon.exceptionMain.CouponSystemException;

public class CustomerNotExistsException extends CouponSystemException {
    private static final long serialVersionUID = 1L;

    public CustomerNotExistsException() {
    }

    public CustomerNotExistsException(String message) {
        super(message);
    }

    public CustomerNotExistsException(Throwable cause) {
        super(cause);
    }

    public CustomerNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerNotExistsException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}