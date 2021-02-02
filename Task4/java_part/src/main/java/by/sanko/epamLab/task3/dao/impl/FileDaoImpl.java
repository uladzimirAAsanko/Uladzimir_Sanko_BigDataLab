package by.sanko.epamLab.task3.dao.impl;

import by.sanko.epamLab.task3.dao.Dao;
import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.entity.StopAndSearch;
import by.sanko.epamLab.task3.exception.DaoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;


public class FileDaoImpl implements Dao {
    private static String DEFAULT_FILENAME = "data.csv";

    @Override
    public void addCrimes(List<Crime> crimes) throws DaoException {
        File csvOutputFile = new File(DEFAULT_FILENAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            for(Crime crime : crimes){
                pw.write(crime.toString());
            }
        } catch (FileNotFoundException e) {
            throw new DaoException("Exception while writing information in file",e);
        }
    }

    @Override
    public void addStopAndSearch(List<StopAndSearch> searches) throws DaoException {
        File csvOutputFile = new File(DEFAULT_FILENAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            for(StopAndSearch stopAndSearch : searches){
                pw.write(stopAndSearch.toString());
            }
        } catch (FileNotFoundException e) {
            throw new DaoException("Exception while writing information in file",e);
        }
    }

    public void writeInFile(List<Crime> crimes, String fileName)  throws DaoException{
        File csvOutputFile = new File(fileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            for(Crime crime : crimes){
                pw.write(crime.toString());
            }
        } catch (FileNotFoundException e) {
            throw new DaoException("Exception while writing information in file",e);
        }
    }

}
