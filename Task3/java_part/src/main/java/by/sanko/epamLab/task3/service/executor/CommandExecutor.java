package by.sanko.epamLab.task3.service.executor;

import by.sanko.epamLab.task3.dao.Dao;
import by.sanko.epamLab.task3.dao.impl.DataBaseDaoImpl;
import by.sanko.epamLab.task3.dao.impl.FileDaoImpl;
import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.entity.Location;
import by.sanko.epamLab.task3.exception.DaoException;
import by.sanko.epamLab.task3.exception.ServiceException;
import by.sanko.epamLab.task3.service.downloader.HttpDownloader;
import by.sanko.epamLab.task3.service.downloader.impl.HttpDownloaderImpl;
import by.sanko.epamLab.task3.util.parser.JSONParser;
import by.sanko.epamLab.task3.util.reader.ReadFile;
import by.sanko.epamLab.task3.util.parser.DateParser;
import by.sanko.epamLab.task3.util.validator.DateValidator;
import by.sanko.epamLab.task3.util.validator.StringValidator;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;

public class CommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    private static Options options = new Options();

    static {
        options.addOption("d",true, "GetDates of download data");
        options.addOption("a",false,"Take all places from file");
        options.addOption("y",true,"Year that we'd like to download");
        options.addOption("m",true,"Month that we'd like to download");
        options.addOption("lat",true,"Latitude we'd like to download");
        options.addOption("lng",true,"Longitude we'd like to download");
        options.addOption("f",true,"Download data to file");
        options.addOption("db",false,"Download data to database");
    }

    public void parseAndExecuteCommands(String[] args) throws ServiceException {
        CommandLineParser clParser = new DefaultParser();
        try {
            CommandLine cmd = clParser.parse( options, args);
            ArrayList<LocalDate> dateArrayList = new ArrayList<>();
            ArrayList<Location> locationArrayList = new ArrayList<>();
            int year = 0;
            int month = -1;
            double latitude = 0;
            double longitude = 0;
            String fileName = "";
            Dao dao = null;
            HttpDownloader downloader = new HttpDownloaderImpl();

            if(cmd.hasOption('d')){
                String dates = cmd.getOptionValue("d");
                if(dates == null){
                    throw new ServiceException("Illegal arguments");
                }
                int dividerPos = dates.indexOf(':');
                LocalDate startDate = DateParser.parseDate(dates.substring(0, dividerPos));
                LocalDate endDate = DateParser.parseDate(dates.substring(dividerPos + 1));
                if(!DateValidator.validateTwoDates(startDate,endDate)){
                    throw new ServiceException("Illegal arguments");
                }
                while (!startDate.isAfter(endDate)){
                    dateArrayList.add(startDate);
                    startDate = startDate.plusMonths(1);
                }
            }
            if (cmd.hasOption('a')){
                locationArrayList = ReadFile.readAllLocations();
            }
            if (cmd.hasOption('y')){
                String data = cmd.getOptionValue("y");
                if(data == null){
                    throw new ServiceException("Illegal arguments");
                }
                year = StringValidator.validateInt(data);
                if(year < 2018){
                    throw new ServiceException("Illegal arguments");
                }
            }
            if (cmd.hasOption('m')){
                String data = cmd.getOptionValue("m");
                if(data == null){
                    throw new ServiceException("Illegal arguments");
                }
                month = StringValidator.validateInt(data);
                if(month < 0 || month > 11){
                    throw new ServiceException("Illegal arguments");
                }
            }
            if (cmd.hasOption("lng")){
                String data = cmd.getOptionValue("lng");
                if(data == null){
                    throw new ServiceException("Illegal arguments");
                }
                longitude = StringValidator.validateDouble(data);
                if(longitude < -5 || longitude > 5){
                    throw new ServiceException("Illegal arguments");
                }
            }
            if (cmd.hasOption("lat")){
                String data = cmd.getOptionValue("lat");
                if(data == null){
                    throw new ServiceException("Illegal arguments");
                }
                latitude = StringValidator.validateDouble(data);
                if(latitude < 0 || latitude > 60){
                    throw new ServiceException("Illegal arguments");
                }
            }
            if(cmd.hasOption("f")){
                String data = cmd.getOptionValue("f");
                if(data == null){
                    dao = new FileDaoImpl();
                }else{
                    fileName = data;
                }
            }
            if(cmd.hasOption("db")){
                dao = new DataBaseDaoImpl();
            }
            if(latitude != 0 && longitude != 0){
                locationArrayList.add(new Location(latitude,longitude));
            }
            if(year != 0 && month != -1){
                dateArrayList.add(LocalDate.of(year,month,1));
            }
            ArrayList<Crime> crimes = new ArrayList<>();
            for(Location location : locationArrayList){
                for(LocalDate date : dateArrayList){
                    ArrayList<Crime> downloadedCrimes = JSONParser.parseWholeText(downloader.sendRequest(
                            location.getLatitude(),location.getLongitude(),date));
                    if(downloadedCrimes != null){
                        crimes.addAll(downloadedCrimes);
                    }
                }
            }
            logger.info("All data downloaded, parsed and added to collection");
            if(dao == null){
                if(fileName.equals("")){
                    throw new ServiceException("No dao has been chosen");
                }
                FileDaoImpl fileDao = new FileDaoImpl();
                try {
                    fileDao.writeInFile(crimes,fileName);
                }catch (DaoException e){
                    throw new ServiceException("Exception while adding information",e);
                }
            }else {
                try {
                    dao.addInformation(crimes);
                }catch (DaoException e){
                    throw new ServiceException("Exception while adding information",e);
                }
            }
            logger.info("All data saved by chosen dao");
        } catch (ParseException e) {
            throw new ServiceException("Exception while parsing command line strings",e);
        }
    }
}
