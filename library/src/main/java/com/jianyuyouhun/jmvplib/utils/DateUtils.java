package com.jianyuyouhun.jmvplib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期转换工具类
 * Created by wangyu on 2017/6/26.
 */

public class DateUtils {

    /**
     * 获取时间字符串对应的毫秒数
     *
     * @param dateString        时间字符串
     * @param formatString      自定义格式，例如 yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static long getTimeMillisecondByDateStringWithFormatString(String dateString, String formatString) {
        long result = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatString, Locale.getDefault());
        try {
            result = dateFormat.parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取毫秒数对应的自定义格式的时间字符串
     *
     * @param milliseconds 毫秒数
     * @param formatString 自定义格式，例如 yyyy年MM月dd日
     * @return
     */
    public static String getDateStringByMillisecondsWithFormatString(long milliseconds, String formatString) {
        Date date = new Date(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 得到几天前的日期
     *
     * @param days          几天前
     * @param formatString  字符串格式
     */
    public static String getFewDaysAgo(int days, String formatString) {
        long millisecond = System.currentTimeMillis() - (days * 24 * 3600 * 1000);
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 得到几天前的日期
     *
     * @param days  几天前
     */
    public static String getFewDaysAgo(int days) {
        return getFewDaysAgo(days, "yy-MM-dd");
    }
}
