package by.sanko.epamLab.task3.util.reader;

import by.sanko.epamLab.task3.entity.Location;
import by.sanko.epamLab.task3.exception.ServiceException;

import java.io.*;
import java.util.ArrayList;

public class ReadFile {
    private static final String FILE_NAME = "./src/main/resources/LondonStations.csv";
    private static final String LINE_SEPARATOR = ",";

    public static ArrayList<Location> readAllLocations() throws ServiceException{
        String line = "";
        ArrayList<Location> locations = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((line = bufferedReader.readLine()) != null){
                String[] elements = line.split(LINE_SEPARATOR);
                double longitude = Double.parseDouble(elements[1]);
                double latitude = Double.parseDouble(elements[2]);
                Location location = new Location(latitude,longitude);
                locations.add(location);
            }
        } catch (FileNotFoundException e) {
            throw new ServiceException("Cannot find file with all locations",e);
        } catch (IOException e) {
            throw new ServiceException("Exception while reading file with all locations",e);
        }
        return locations;
    }
}
