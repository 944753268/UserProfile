package com.canbot.userprofile.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static String millisToDate(long millis) {
        Date nowTime = new Date(millis);
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdFormatter.format(nowTime);
    }

    public static String getNowDate(long millis) {
        Date nowTime = new Date(millis);
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd");
        return sdFormatter.format(nowTime);
    }
}
