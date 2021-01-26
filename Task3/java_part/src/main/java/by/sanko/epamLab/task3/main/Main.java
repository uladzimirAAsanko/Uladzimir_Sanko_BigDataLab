package by.sanko.epamLab.task3.main;

import by.sanko.epamLab.task3.dao.Dao;
import by.sanko.epamLab.task3.dao.impl.DataBaseDaoImpl;
import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.exception.DaoException;
import by.sanko.epamLab.task3.dao.impl.FileDaoImpl;
import by.sanko.epamLab.task3.parser.JSONParser;
import by.sanko.epamLab.task3.service.impl.HttpDownloaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Starting to performance the program");
        System.out.println("Starting to performance the program");
        HttpDownloaderImpl downloader = new HttpDownloaderImpl();
        if(args == null){
            System.out.println("User dont write any flags");
            logger.error("User dont write any flags");
            return;
        }
        Dao dao = null;
        int year = 0;
        int month = 0;
        double lng = 0;
        double lat = 0;

        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-file")){
                dao = new FileDaoImpl();
            }
            if(args[i].equals("-db")) {
                dao = new DataBaseDaoImpl();
            }
            if(args[i].equals("-y")){
                try {
                    year = Integer.parseInt(args[i + 1]);
                    i++;
                }catch (NumberFormatException | IndexOutOfBoundsException exception){
                    System.out.println("Illegal input");
                    logger.error("Illegal input");
                }
            }
            if(args[i].equals("-m")){
                try {
                    month = Integer.parseInt(args[i + 1]);
                    i++;
                }catch (NumberFormatException | IndexOutOfBoundsException exception){
                    System.out.println("Illegal input");
                    logger.error("Illegal input");
                }
            }
            if(args[i].equals("-lng")){
                try {
                    lng = Double.parseDouble(args[i + 1]);
                    i++;
                }catch (NumberFormatException | IndexOutOfBoundsException exception){
                    System.out.println("Illegal input");
                    logger.error("Illegal input");
                }
            }
            if(args[i].equals("-lat")){
                try {
                    lat = Double.parseDouble(args[i + 1]);
                    i++;
                }catch (NumberFormatException | IndexOutOfBoundsException exception){
                    System.out.println("Illegal input");
                    logger.error("Illegal input");
                }
            }
        }
        System.out.println("Year = "+year);
        System.out.println("Month = "+month);
        System.out.println("lat = "+lat);
        System.out.println("lng = "+lng);
        LocalDate date = LocalDate.of(year,month,1);
        String answer = downloader.sendRequest(lat,lng, date);
        JSONParser jsonParser = new JSONParser();
        ArrayList<Crime> list = jsonParser.parseWholeText(answer);
        if(list == null || list.isEmpty() || dao == null){
            System.out.println("Downloaded list of crimes data is null or empty");
            logger.info("Downloaded list of crimes data is null or empty");
        }else{
            System.out.println("Downloaded list of crimes data is ready ");
            logger.info("Downloaded list of crimes data is ready ");
            try {
                dao.addInformation(list);
            } catch (DaoException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
