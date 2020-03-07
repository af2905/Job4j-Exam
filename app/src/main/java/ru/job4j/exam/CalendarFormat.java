package ru.job4j.exam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarFormat {
    public static String dateFormatMethod() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}
