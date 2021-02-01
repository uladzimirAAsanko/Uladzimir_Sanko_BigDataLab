package by.sanko.epamLab.task3.util.parser;

import by.sanko.epamLab.task3.entity.Crime;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {
    private static final Logger logger = LoggerFactory.getLogger(JSONParser.class);

    public static ArrayList<Crime> parseWholeText(String data){
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Crime> crimes = null;
        try {
            crimes = mapper.readValue(data, new TypeReference<List<Crime>>(){});
        } catch (IOException e) {
            logger.error("Exception while parsing JSON");
        }
        return crimes;
    }
}
