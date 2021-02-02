package by.sanko.epamLab.task3.util.parser;

import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.entity.Force;
import by.sanko.epamLab.task3.entity.StopAndSearch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {
    private static final Logger logger = LoggerFactory.getLogger(JSONParser.class);

    public static ArrayList<Crime> parseCrimesArray(String data){
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Crime> crimes = null;
        try {
            crimes = mapper.readValue(data, new TypeReference<List<Crime>>(){});
        } catch (IOException e) {
            logger.error("Exception while parsing JSON");
        }
        return crimes;
    }

    public static ArrayList<Force> parseForceArray(String data){
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Force> forceArrayList = null;
        try {
            forceArrayList = mapper.readValue(data, new TypeReference<List<Force>>(){});
        } catch (IOException e) {
            logger.error("Exception while parsing JSON");
        }
        return forceArrayList;
    }

    public static ArrayList<StopAndSearch> parseStopArray(String data){
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<StopAndSearch> sasList = null;
        try {
            sasList = mapper.readValue(data, new TypeReference<List<StopAndSearch>>(){});
        } catch (IOException e) {
            logger.error("Exception while parsing JSON");
        }
        return sasList;
    }
}
