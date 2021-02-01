package by.sanko.epamLab.task3.util.parser;

import java.time.LocalDate;

public class DateParser {
    private static final String DASH_MARK = "-";

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
}
