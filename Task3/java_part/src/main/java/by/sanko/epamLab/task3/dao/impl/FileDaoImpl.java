package by.sanko.epamLab.task3.dao.impl;

import by.sanko.epamLab.task3.dao.Dao;
import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.exception.DaoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;


public class FileDaoImpl implements Dao {
    private static final String FILE_NAME = "data.csv";

    @Override
    public boolean addInformation(List<Crime> crimes) throws DaoException {
        File csvOutputFile = new File(FILE_NAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            crimes.stream()
                    .map((Function<Crime, Object>) Crime::toString)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            throw new DaoException(e.getMessage());
        }
        return true;
    }

}
