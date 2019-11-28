package ghost_coupon.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Date generator class
 * 
 * Creating dates for use
 **/

public class DateGenerator {
    public static Date getDateAfterMonths(int months) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }
}
