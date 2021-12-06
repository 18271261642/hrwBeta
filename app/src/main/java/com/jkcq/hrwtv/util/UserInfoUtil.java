package com.jkcq.hrwtv.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by peng on 2018/5/9.
 */

public class UserInfoUtil {

    public static int parseAge(long time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = dateFormat.format(new Date(time));
        Date date = dateFormat.parse(birthday);
        Calendar calendar = Calendar.getInstance();
        Calendar currentcalendar = Calendar.getInstance();
        calendar.setTime(date);
        return currentcalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
    }
}
