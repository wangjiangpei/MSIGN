package com.dscs.sign.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chowtoni on 16/2/10.
 */
public class DateUtil {
    public static final String SECONDS_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "yyyyMMddHHmm";
    public static final String TIME_DATA = "yyyy/MM/dd";
    public static Date parseDate(String formate, String dateString) {
        try {
            new SimpleDateFormat(formate).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formateDate(String formate, Date date) {
        return new SimpleDateFormat(formate).format(date);
    }

    public static String getCurrent() {
        return DateUtil.formateDate(DEFAULT_FORMAT, new Date());
    }
    public static String getDefaultDate(String oldDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldDate);
        String newDate  = new SimpleDateFormat("yyyyMMdd").format(date);
        return newDate;
    }
    public static String getTimeCurrent() {
        return DateUtil.formateDate(TIME_FORMAT, new Date());
    }
    public static String getSecondsCurrent() {
        return DateUtil.formateDate(SECONDS_FORMAT, new Date());
    }
}
