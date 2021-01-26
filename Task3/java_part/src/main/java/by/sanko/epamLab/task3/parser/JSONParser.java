package by.sanko.epamLab.task3.parser;

import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.entity.Location;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONParser {
    private static final String regex = "\\{\"category\":";
    private static final String regexA = "},\\{\"category\":";
    private static final String CATEGORY_PARAM = "\"category\"";
    private static final String LOCATION_TYPE_PARAM = "\"location_type\"";
    private static final String QT_MARK = "\"";
    private static final String BRACE_MARK = "}";
    private static final String DASH_MARK = "-";
    private static final String LATITUDE_MARK = "\"latitude\"";
    private static final String LONGITUDE_MARK = "\"longitude\"";
    private static final String CONTEXT_MARK = "\"context\"";
    private static final String OUT_STATUS_MARK = "\"outcome_status\"";
    private static final String PERSISTENT_ID_MARK = "\"persistent_id\"";
    private static final String LOCATION_SUBTYPE_MARK = "\"location_subtype\"";
    private static final String MONTH_MARK = "\"month\"";
    private static final String DATE_MARK = "\"date\"";
    private static final String NAME_MARK = "\"name\"";
    private static final String STREET_MARK = "\"street\"";
    private static final String ID_MARK = "\"id\"";
    private static final char COMMA_MARK = ',';


    public ArrayList<Crime> parseWholeText(String data){
        Pattern pattern = Pattern.compile(regexA);
        Matcher matcher = pattern.matcher(data);
        matcher.find();
        int firstFind = matcher.start();
        matcher.find();
        int secondFind = matcher.start();
        ArrayList<Crime> list = new ArrayList<>();
        Crime crime = parsePart(data.substring(0,firstFind));
        while(matcher.find()){
            crime = parsePart(data.substring(firstFind, secondFind));
            firstFind = secondFind;
            secondFind = matcher.start();
            list.add(crime);
        }
        return list;
    }

    private Crime parsePart(String data){
        String category = findDef(data,CATEGORY_PARAM);
        String locationType = findDef(data, LOCATION_TYPE_PARAM);
        String streetPart = bigPart(data,STREET_MARK);
        int id = findDefInt(streetPart, ID_MARK);
        String name = findDef(streetPart, NAME_MARK);
        Location location = new Location(Double.parseDouble(findDef(data, LATITUDE_MARK)),
        Double.parseDouble(findDef(data, LONGITUDE_MARK)),id,name);
        String context = findDef(data,CONTEXT_MARK);
        String persistent = findDef(data,PERSISTENT_ID_MARK);
        String locationSubtype = findDef(data,LOCATION_SUBTYPE_MARK);
        String dates = findDef(data, MONTH_MARK);
        int year = Integer.parseInt(dates.substring(0, dates.indexOf(DASH_MARK)));
        int month = Integer.parseInt(dates.substring(dates.indexOf(DASH_MARK) + 1));
        LocalDate localDate = LocalDate.of(year, month, 1);
        String stringWithID = data.substring(data.indexOf(PERSISTENT_ID_MARK));
        long idMain = findDefInt(stringWithID,ID_MARK);

        String outcome = bigPart(data,OUT_STATUS_MARK);
        String outcomeStatusDateString = "";
        String outcomeStatusCategory = "";
        int outcomeYear = -1;
        int outcomeMonth = -1;
        LocalDate date = null;
        if(!outcome.equals("null")){
            outcomeStatusCategory = findDef(outcome, CATEGORY_PARAM);
            outcomeStatusDateString = findDef(outcome,DATE_MARK );
            outcomeMonth = Integer.parseInt(outcomeStatusDateString.substring(outcomeStatusDateString.indexOf(DASH_MARK) + 1));
            outcomeYear = Integer.parseInt(outcomeStatusDateString.substring(0, outcomeStatusDateString.indexOf(DASH_MARK)));
            date = LocalDate.of(outcomeYear, outcomeMonth, 1);
        }
        Crime crime = new Crime(category,locationType,location, context ,outcomeStatusCategory,date, persistent,
                idMain, locationSubtype,localDate);
        return crime;
    }

    private String findDef(String data, String keyWord){
        String answer = "";
        int indexOfStart = data.indexOf(QT_MARK,data.indexOf(keyWord) + keyWord.length()) + 1;
        int indexOfEnd = data.indexOf(QT_MARK,indexOfStart);
        answer = data.substring(indexOfStart,indexOfEnd);
        return answer;
    }

    private int findDefInt(String data,String keyWord){
        String answer = "";
        int indexOfStart = data.indexOf(keyWord) + keyWord.length() + 1;
        int indexOfEnd = data.indexOf(COMMA_MARK,indexOfStart);
        answer = data.substring(indexOfStart,indexOfEnd -1);

        return Integer.parseInt(answer);
    }

    private String bigPart(String data, String keyWord){
        String answer = "";
        int indexOfStart = data.indexOf(keyWord) + keyWord.length() + 3;
        int indexOfEnd = data.indexOf(BRACE_MARK,indexOfStart);
        if (indexOfEnd < 0){
            answer = "null";
        }else{
            answer = data.substring(indexOfStart, indexOfEnd);
        }
        return answer;
    }


}
