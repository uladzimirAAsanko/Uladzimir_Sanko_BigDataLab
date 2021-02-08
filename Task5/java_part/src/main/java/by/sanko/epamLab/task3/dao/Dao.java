package by.sanko.epamLab.task3.dao;

import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.entity.StopAndSearch;
import by.sanko.epamLab.task3.exception.DaoException;

import java.util.List;

public interface Dao {
    void addCrimes(List<Crime> crimes) throws DaoException;

    void addStopAndSearch(List<StopAndSearch> searches) throws DaoException;
}
