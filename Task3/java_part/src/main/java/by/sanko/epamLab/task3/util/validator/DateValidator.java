package by.sanko.epamLab.task3.util.validator;

import java.time.LocalDate;

public class DateValidator {
    public static boolean validateTwoDates(LocalDate startDate, LocalDate endDate){
        if (!validateDate(startDate)){
            return false;
        }
        if(!validateDate(endDate)){
            return false;
        }
        return !startDate.isAfter(endDate);
    }

    public static boolean validateDate(LocalDate date){
        if(date == null){
            return false;
        }
        if(date.getYear() > 2020 || date.getYear() < 2017 ){
            return false;
        }
        return true;
    }
}
