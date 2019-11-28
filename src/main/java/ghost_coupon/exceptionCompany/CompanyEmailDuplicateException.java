package ghost_coupon.exceptionCompany;

import ghost_coupon.exceptionMain.CouponSystemException;

public class CompanyEmailDuplicateException extends CouponSystemException {
    private static final long serialVersionUID = 1L;

    public CompanyEmailDuplicateException() {
    }

    public CompanyEmailDuplicateException(String message) {
        super(message);
    }

    public CompanyEmailDuplicateException(Throwable cause) {
        super(cause);
    }

    public CompanyEmailDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompanyEmailDuplicateException(String message, Throwable cause, boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
