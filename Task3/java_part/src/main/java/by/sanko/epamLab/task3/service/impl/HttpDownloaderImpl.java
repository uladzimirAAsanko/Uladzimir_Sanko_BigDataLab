package by.sanko.epamLab.task3.service.impl;

import by.sanko.epamLab.task3.main.Main;
import by.sanko.epamLab.task3.service.HttpDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalDate;

public class HttpDownloaderImpl implements HttpDownloader {

    private static final String BASE_URL = "https://data.police.uk/api/crimes-street/all-crime";
    private static final String LATITUDE_PARAM = "lat";
    private static final String LONGITUDE_PARAM = "lng";
    private static final String DATE_PARAM = "date";
    private static final String QUESTION_MARK = "?";
    private static final String AND_MARK = "&";
    private static final String EQ_MARK = "=";
    private static final String DEF_MARK = "-";

    @Override
    public String sendRequest(double lat, double lng, LocalDate date) {
        Logger logger = LoggerFactory.getLogger(HttpDownloaderImpl.class);
        String answer = "";
        int year = date.getYear();
        int month = date.getMonthValue();
        String request = BASE_URL + QUESTION_MARK + LATITUDE_PARAM + EQ_MARK + lat + AND_MARK +
                LONGITUDE_PARAM + EQ_MARK + lng +
                AND_MARK + DATE_PARAM + EQ_MARK + year + DEF_MARK + month;
        try (BufferedInputStream in = new BufferedInputStream(new URL(request).openStream())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead = 0;
            StringBuilder builder = new StringBuilder();
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                builder.append(new String(dataBuffer,0,bytesRead));
            }
            answer = builder.toString();
        } catch (IOException e) {
            logger.error("You dont have ethernet or request is bad");
        }
        return answer;
    }
}