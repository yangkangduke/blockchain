package com.seeds.common.utils;

import com.seeds.common.exception.DataFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.FastDateFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author milo
 */
@Slf4j
public class BasicUtils {

    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String TIME_FORMAT = "HH:mm";

    public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    /**
     * 每秒毫秒数
     */
    public static final long SECOND = TimeUnit.SECONDS.toMillis(1);
    /**
     * 每分钟毫秒数
     */
    public static final long MINUTE = TimeUnit.MINUTES.toMillis(1);
    /**
     * 每小时毫秒数
     */
    public static final long HOUR = TimeUnit.HOURS.toMillis(1);
    /**
     * 每天毫秒数
     */
    public static final long DAY = TimeUnit.DAYS.toMillis(1);

    /**
     * 防止价格攻击
     *
     * @param a
     * @return
     */
    public static boolean validate(BigDecimal a) {
        return validate(a, 18, 8);
    }

    public static boolean validate(BigDecimal a, int maxPrecision, int maxScale) {
        if (a == null) {
            return false;
        }
        BigDecimal value = a.stripTrailingZeros();
        return value.precision() <= maxPrecision && value.scale() <= maxScale;
    }

    public static boolean equals(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a instanceof BigDecimal && b instanceof BigDecimal) {
            return ((BigDecimal) a).compareTo((BigDecimal) b) == 0;
        }
        return a.equals(b);
    }

    public static BigDecimal add(BigDecimal a, BigDecimal... b) {
        BigDecimal c = a;
        for (BigDecimal d : b) {
            c = c.add(d);
        }
        return c;
    }

    public static BigDecimal sub(BigDecimal a, BigDecimal... b) {
        BigDecimal c = a;
        for (BigDecimal d : b) {
            c = c.subtract(d);
        }
        return c;
    }

    public static BigDecimal mul(BigDecimal a, BigDecimal... b) {
        BigDecimal c = a;
        for (BigDecimal d : b) {
            c = c.multiply(d);
        }
        return c;
    }

    public static BigDecimal div(BigDecimal a, BigDecimal... b) {
        BigDecimal c = a;
        for (BigDecimal d : b) {
            c = c.divide(d, 18, RoundingMode.HALF_EVEN);
        }
        return c;
    }

    public static BigDecimal min(BigDecimal... a) {
        if (a == null || a.length == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal value = a[0];
        for (int i = 1; i < a.length; i++) {
            value = value.compareTo(a[i]) <= 0 ? value : a[i];
        }
        return value;
    }

    public static BigDecimal max(BigDecimal... a) {
        if (a == null || a.length == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal value = a[0];
        for (int i = 1; i < a.length; i++) {
            value = value.compareTo(a[i]) >= 0 ? value : a[i];
        }
        return value;
    }

    /**
     * 限制数据的范围
     *
     * @param value
     * @param minValue
     * @param maxValue
     * @return
     */
    public static BigDecimal bound(BigDecimal value, BigDecimal minValue, BigDecimal maxValue) {
        if (value.compareTo(minValue) < 0) {
            return minValue;
        } else if (value.compareTo(maxValue) > 0) {
            return maxValue;
        } else {
            return value;
        }
    }

    public static long toSeconds() {
        return (System.currentTimeMillis() / SECOND) * SECOND;
    }

    public static long toMinutes() {
        return (System.currentTimeMillis() / MINUTE) * MINUTE;
    }

    public static long toHours() {
        return (System.currentTimeMillis() / HOUR) * HOUR;
    }

    public static long toHours(long timestamp) {
        return (timestamp / HOUR) * HOUR;
    }

    /**
     * minutes level timestamp: current - 24hr
     *
     * @param timestamp
     * @return
     */
    public static long toPreDayMinutes(long timestamp) {
        return (timestamp / MINUTE * MINUTE) - DAY;
    }

    /**
     * 上一结算时间戳（hour level）
     *
     * @param currentTimestamp
     * @param preHour
     * @param currentHour
     * @return
     */
    public static long toPreSettleHours(long currentTimestamp, int preHour, int currentHour) {
        return (currentTimestamp / HOUR) * HOUR - TimeUnit.HOURS.toMillis(getHourDuration(preHour, currentHour));
    }

    /**
     * 小时差 =｜start - end｜， 24小时计
     * e.g.
     * start = 11, end = 15, then 4
     * start = 20, end = 1, then 5
     *
     * @param start day-of-hour 0~23
     * @param end   day-of hour 0 ~23
     * @return
     */
    public static int getHourDuration(int start, int end) {
        if (start > 23 || start < 0 ||
                end > 23 || end < 0) {
            throw new DataFormatException("invalid 24 hrs format");
        }
        if (end == 0) {
            return start;
        }
        if (start == end) {
            return 24;
        }
        return end < start ? 24 - (start - end) : end - start;
    }

    /**
     * round to utc day start time.
     *
     * @return
     */
    public static long toDays() {
        return (System.currentTimeMillis() / DAY) * DAY;
    }

    /**
     * Gets the UTC hour-of-day 0 ~ 23
     *
     * @param timestamp timestamp in second
     * @return
     */
    public static int getHour(int timestamp) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
        return localDateTime.getHour();
    }

    /**
     * Gets the UTC hour-of-day 0 ~ 23
     * e.g. 8:00 -> 8
     * 8:01 -> 9
     * 23:59 -> 0
     *
     * @param timestamp timestamp in millis
     * @return
     */
    public static int getHour(long timestamp) {
        long mod = timestamp % HOUR;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC);
        if (mod != 0) {
            // 非整点
            return localDateTime.getHour() == 23 ? 0 : localDateTime.getHour() + 1;
        }

        return localDateTime.getHour();
    }

    /**
     * 返回当日特定时间的 minute level 时间戳
     *
     * @param hourOfDay      the hour-of-day 0 ~ 23
     * @param minuteOfHour   the minute-of-hour 0 ~ 59
     * @param secondOfMinute the second-of-minute 0 ~ 59
     * @return
     */
    public static Long getSpecificTimestamp(int hourOfDay, int minuteOfHour, int secondOfMinute) {
        return getSpecificTimestamp(hourOfDay, minuteOfHour, secondOfMinute, 0);
    }

    /**
     * 返回特定时间的 minute level 时间戳
     *
     * @param hourOfDay      the hour-of-day 0 ~ 23
     * @param minuteOfHour   the minute-of-hour 0 ~ 59
     * @param secondOfMinute the second-of-minute 0 ~ 59
     * @param rollDate       if != 0, will roll to next rollDate, e.g. = 1, roll to tomorrow
     * @return
     */
    public static Long getSpecificTimestamp(int hourOfDay, int minuteOfHour, int secondOfMinute, int rollDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH) + rollDate, hourOfDay, minuteOfHour, secondOfMinute);
        return calendar.getTimeInMillis() / SECOND * SECOND;
    }

    public static String formatTime(String format, long time) {
        return FastDateFormat.getInstance(format).format(time);
    }

    public static String formatToDate(long time) {
        return formatTime("yyyy-MM-dd", time);
    }

    public static String formatToTime(long time) {
        return formatTime("HH:mm:ss", time);
    }

    public static String formatToFullTime(long time) {
        return formatTime("yyyy-MM-dd HH:mm:ss", time);
    }

    /**
     * 获取当前结算时间戳index
     *
     * e.g. BTC 结算区间为0，8，16 当前为9:01 则当前结算时间为16:00
     *
     * @param settleHourTimestampList
     * @param timestamp
     * @return
     */
    public static int getSettleIndex(List<Long> settleHourTimestampList, long timestamp) {
        if (settleHourTimestampList.size() < 5) {
            // 结算时间表至少有5个元素，前两个结算时间戳 + 当前结算时间戳 + 下两个结算时间戳
            return -1;
        }
        if (timestamp < settleHourTimestampList.get(1) || timestamp < toDays()) {
            return -1;
        }
        if (timestamp <= settleHourTimestampList.get(2)) {
            return 2;
        }
        //(] 前开后闭
        for (int i = 2; i < settleHourTimestampList.size() - 2; i++) {
            if (settleHourTimestampList.get(i) < timestamp &&
                    settleHourTimestampList.get(i + 1) >= timestamp) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * 获取前一结算时间戳index
     *
     * e.g. BTC 结算区间为0，8，16 当前为9:01 则当前结算时间为8:00
     *
     * @param settleHourTimestampList
     * @param timestamp
     * @return
     */
    public static int getPreSettleIndex(List<Long> settleHourTimestampList, long timestamp) {
        if (settleHourTimestampList.size() < 5) {
            // 结算时间表至少有5个元素，前两个结算时间戳 + 当前结算时间戳 + 下两个结算时间戳
            return -1;
        }
        if (timestamp < settleHourTimestampList.get(1) || timestamp < toDays()) {
            return -1;
        }
        if (timestamp <= settleHourTimestampList.get(2)) {
            return 1;
        }
        //(] 前开后闭
        for (int i = 2; i < settleHourTimestampList.size() - 2; i++) {
            if (settleHourTimestampList.get(i) < timestamp &&
                    settleHourTimestampList.get(i + 1) >= timestamp) {
                return i;
            }
        }
        return -1;
    }

    public static void sleepFor(long timeout) {
        if (timeout > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(timeout);
            } catch (InterruptedException tx) {
                log.error("sleepFor timeout={}", timeout, tx);
            }
        }
    }
}
