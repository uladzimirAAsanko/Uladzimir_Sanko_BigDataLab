package by.sanko.epamLab.task3.util.validator;

public class StringValidator {

    public static int validateInt(String data){
        int answer = -1;
        try{
            answer = Integer.parseInt(data);
        }catch (NumberFormatException e){
            return -1;
        }
        return answer;
    }

    public static double validateDouble(String data){
        double answer = -1;
        try{
            answer = Double.parseDouble(data);
        }catch (NumberFormatException e){
            return -1;
        }
        return answer;
    }
}
