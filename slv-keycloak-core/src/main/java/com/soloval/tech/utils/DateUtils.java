package com.soloval.tech.utils;



import com.soloval.tech.constants.CommonConstants;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private DateUtils() {
    }

    public static String getCurrentTimeString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CommonConstants.FORMAT_DATE_TIME);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(CommonConstants.ZONE_ID));
        return dateTimeFormatter.format(zonedDateTime);
    }

    public static String getCurrentTimeString(String zoneId) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CommonConstants.FORMAT_DATE_TIME);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(zoneId));
        return dateTimeFormatter.format(zonedDateTime);
    }

    public static ZonedDateTime getCurrentTime() {
        return ZonedDateTime.now();
    }

    public static ZonedDateTime getCurrentTime(String zoneId) {
        return ZonedDateTime.now(ZoneId.of(zoneId));
    }
}
