package by.sanko.epamLab.task3.service.downloader.impl;

import by.sanko.epamLab.task3.exception.ServiceException;
import by.sanko.epamLab.task3.service.downloader.HttpDownloader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;


public class HttpDownloaderImpl implements HttpDownloader {
    private static final Logger logger = LoggerFactory.getLogger(HttpDownloaderImpl.class);
    private static final String BASE_URL = "https://data.police.uk/api/crimes-street/all-crime";
    private static final String DEF_MARK = "-";
    private static final String LAT_PART = "?lat=";
    private static final String LANG_PART = "&lng=";
    private static final String DATE_PART = "&date=";
    private static final int NOT_FOUND_CODE = 404;

    @Override
    public String sendRequest(double lat, double lng, LocalDate date) throws ServiceException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String answer = "";
        int year = date.getYear();
        int month = date.getMonthValue();
        String request = BASE_URL + LAT_PART + lat + LANG_PART + lng + DATE_PART + year + DEF_MARK + month;
        try {
            HttpGet httpRequest = new HttpGet(request);
            HttpResponse response = client.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            int responseCode = response.getStatusLine().getStatusCode();
            InputStream inputStream = entity.getContent();
            if(responseCode == NOT_FOUND_CODE ){
                logger.error("Bad api requests or bad internet connection");
                throw new ServiceException("Bad api requests or bad internet connection");
            }
            if(responseCode > 499){
                logger.error("Connection issue");
                throw new ServiceException("Connection issue");
            }

            byte[] dataBuffer = new byte[1024];
            int bytesRead = 0;
            StringBuilder builder = new StringBuilder();
            while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1) {
                builder.append(new String(dataBuffer,0,bytesRead));
            }
            answer = builder.toString();
        } catch (IOException e) {
            logger.error("Some troubles with downloading data");
            throw new ServiceException(e);
        }
        return answer;
    }
}