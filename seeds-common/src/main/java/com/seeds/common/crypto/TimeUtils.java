package com.seeds.common.crypto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/21
 */

public class TimeUtils {

    private TimeUtils() {
    }

    private final static DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static String getCurrentTimestamp() {
        return DT_FORMAT.format(nowInUTC());
    }

    public static ZonedDateTime parse(String timestamp) {
        return LocalDateTime.parse(timestamp, DT_FORMAT).atZone(ZoneId.of("UTC"));
    }

    public static ZonedDateTime nowInUTC() {
        return Instant.now().atZone(ZoneId.of("UTC"));
    }
}
