package core.task1_2;


import utils.StringUtils;

public class Utils {
    public static boolean isAllPositiveNumbers(String... str){
        for(String string: str){
            if(!StringUtils.isPositiveNumber(string)){
                return false;
            }
        }
        return true;
    }
}
