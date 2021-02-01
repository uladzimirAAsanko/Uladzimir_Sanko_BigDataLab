package by.sanko.epamLab.task3.service.downloader;

import by.sanko.epamLab.task3.exception.ServiceException;

import java.time.LocalDate;
import java.util.ArrayList;

public interface HttpDownloader {
    String sendRequest(double lat, double lng, LocalDate date) throws ServiceException;
}
