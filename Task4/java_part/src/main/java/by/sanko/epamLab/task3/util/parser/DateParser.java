package by.sanko.epamLab.task3.util.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateParser {
    private static final String DASH_MARK = "-";
    private static final String T_MARK = "T";
    private static final String DOUBLE_MARK = ":";
    private static final String PLUS_MARK = "+";

    public static LocalDate parseDate(String data){
        try {
            int year = Integer.parseInt(data.substring(0, data.indexOf(DASH_MARK)));
            int monthParsed = Integer.parseInt(data.substring(data.indexOf(DASH_MARK) + 1));
            LocalDate date = LocalDate.of(year,monthParsed,1);
            return date;
        }catch (NumberFormatException e){
            return null;
        }
    }

    public static LocalDateTime parseDateTime(String data){
        try {
            int year = Integer.parseInt(data.substring(0, data.indexOf(DASH_MARK)));
            data = data.substring(data.indexOf(DASH_MARK) + 1);
            int monthParsed = Integer.parseInt(data.substring(0,data.indexOf(DASH_MARK)));
            data = data.substring(data.indexOf(DASH_MARK) + 1);
            int day = Integer.parseInt(data.substring(0,data.indexOf(T_MARK)));
            data = data.substring(data.indexOf(T_MARK) + 1);
            int hour = Integer.parseInt(data.substring(0 ,data.indexOf(DOUBLE_MARK)));
            data = data.substring(data.indexOf(DOUBLE_MARK) + 1);
            int minute = Integer.parseInt(data.substring(0, data.indexOf(DOUBLE_MARK)));
            data = data.substring(data.indexOf(DOUBLE_MARK) + 1);
            int seconds = Integer.parseInt(data.substring(0 ,data.indexOf(PLUS_MARK)));
            LocalDateTime dateTime = LocalDateTime.of(year,monthParsed,day,hour,minute,seconds);
            return dateTime;
        }catch (NumberFormatException e){
            return null;
        }
    }
}
