package by.sanko.epamLab.task3.service.downloader;

import by.sanko.epamLab.task3.entity.Force;
import by.sanko.epamLab.task3.exception.ServiceException;

import java.time.LocalDate;
import java.util.ArrayList;

public interface HttpDownloader {
    String downloadCrimes(double lat, double lng, LocalDate date) throws ServiceException;

    String downloadForces() throws ServiceException;

    String downloadStopAndSearch(Force force, LocalDate date) throws ServiceException;
}
