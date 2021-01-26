package utils;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {
    public static boolean isPositiveNumber(String str){
        boolean isPositive = false;
        if(isStringContainsNumeric(str)){
            double number = Double.parseDouble(str);
            isPositive = number > 0;
        }
        return isPositive;
    }

    private static boolean isStringContainsNumeric(String str){
        if(org.apache.commons.lang3.StringUtils.isAnyEmpty(str)){
            return false;
        }
        return NumberUtils.isParsable(str);
    }
}