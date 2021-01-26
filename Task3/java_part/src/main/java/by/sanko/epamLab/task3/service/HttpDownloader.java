package by.sanko.epamLab.task3.service;

import java.time.LocalDate;
import java.util.ArrayList;

public interface HttpDownloader {
    String sendRequest(double lat, double lng, LocalDate date);
}
