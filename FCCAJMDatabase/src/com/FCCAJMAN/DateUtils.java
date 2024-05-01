package com.FCCAJMAN;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String DATE_FORMAT = "dd MMM yyyy";
    private static final String DEFAULT_DATE_STRING = "N/A";

    public static Date parseDate(String dateString) throws ParseException {
        if (DEFAULT_DATE_STRING.equalsIgnoreCase(dateString.trim())) {
            // Return a special value for "N/A" or set it to null
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            return dateFormat.parse(dateString);
        }
    }
}
