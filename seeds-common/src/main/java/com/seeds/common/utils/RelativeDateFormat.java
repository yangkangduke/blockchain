package com.seeds.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author hang.yu
 */
@Slf4j
public class RelativeDateFormat {

    private static final long MILLI_SECOND = 1000L;
    private static final long SECOND_MINUTE = 60L;
    private static final long MINUTE_HOUR = 60L;
    private static final long HOUR_DAY = 24L;
    private static final long DAY_MONTH = 30L;
    private static final long MONTH_YEAR = 12L;

    private static final String ONE_SECOND_AGO = "s ago";
    private static final String ONE_MINUTE_AGO = "m ago";
    private static final String ONE_HOUR_AGO = "h ago";
    private static final String ONE_DAY_AGO = "d ago";
    private static final String ONE_MONTH_AGO = "mo ago";
    private static final String ONE_YEAR_AGO = "y ago";

    public static String convert(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long timeDifference = endTime - startTime;
        long second = timeDifference / MILLI_SECOND;
        if (second < SECOND_MINUTE) {
            return second + ONE_SECOND_AGO;
        } else {
            long minute = second / SECOND_MINUTE;
            if (minute < MINUTE_HOUR) {
                return minute + ONE_MINUTE_AGO;
            } else {
                long hour = minute / MINUTE_HOUR;
                if (hour < HOUR_DAY) {
                    return hour + ONE_HOUR_AGO;
                } else {
                    long day = hour / HOUR_DAY;
                    if (day < DAY_MONTH) {
                        return day + ONE_DAY_AGO;
                    } else {
                        long month = day / DAY_MONTH;
                        if (month < MONTH_YEAR) {
                            return day + ONE_MONTH_AGO;
                        } else {
                            long year = month / MONTH_YEAR;
                            return year + ONE_YEAR_AGO;
                        }
                    }
                }
            }
        }
    }

    public static String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;

        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("d");
        }
        if (hour > 0) {
            sb.append(hour).append("h");
        }
        if (minute > 0) {
            sb.append(minute).append("m");
        } else {
            sb.append(0).append("m");
        }
        return sb.toString();
    }

}