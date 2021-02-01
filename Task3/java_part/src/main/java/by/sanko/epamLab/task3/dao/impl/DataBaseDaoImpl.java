package by.sanko.epamLab.task3.dao.impl;

import by.sanko.epamLab.task3.dao.Dao;
import by.sanko.epamLab.task3.dao.pool.ConnectionPool;
import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.entity.Location;
import by.sanko.epamLab.task3.entity.OutcomeStatus;
import by.sanko.epamLab.task3.entity.Street;
import by.sanko.epamLab.task3.exception.DaoException;
import by.sanko.epamLab.task3.service.downloader.impl.HttpDownloaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DataBaseDaoImpl implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(HttpDownloaderImpl.class);
    private static final String INSERT_STREET = "INSERT INTO crime_api.streets VALUES(?,?)";
    private static final String INSERT_LOCATION = "INSERT INTO crime_api.locations VALUES (DEFAULT,?,?,?);";
    private static final String INSERT_OUTCOME_STATUS = "INSERT INTO crime_api.outcome_statuses VALUES (DEFAULT,?,?);";
    private static final String INSERT_CRIME = "INSERT INTO crime_api.crimes VALUES(?,?,?,?,?,?,?,(SELECT location_id FROM crime_api.locations" +
            " WHERE longitude=? AND latitude = ? AND street_id =?),(SELECT outcome_status_id FROM crime_api.outcome_statuses " +
            "WHERE outcome_statuses.category=? AND date=?));";
    private static final String INSERT_CRIME_WITHOUT_STATUS = "INSERT INTO crime_api.crimes VALUES(?,?,?,?,?,?,?," +
            "(SELECT location_id FROM crime_api.locations WHERE longitude=? AND latitude = ? AND street_id =?),null);";
    private static final String FIND_STREET = "SELECT * FROM crime_api.streets WHERE street_id =?;";
    private static final String FIND_LOCATION = "SELECT * FROM crime_api.locations WHERE  longitude=? AND latitude=?;";
    private static final String FIND_STATUS = "SELECT * FROM crime_api.outcome_statuses WHERE  category=? AND date=?;";
    private static final String FIND_CRIMES = "SELECT * FROM crime_api.crimes WHERE  crimes_id=?;";



    @Override
    public void addInformation(List<Crime> crimes) throws DaoException {
        int crimesAdded = 0;
        int statusesAdded = 0;
        int streetsAdded = 0;
        int locationsAdded = 0;
        for(Crime crime : crimes){
            if(insertStreet(crime.getLocation().getStreet())){
                streetsAdded++;
            }
            if(insertLocation(crime.getLocation())){
                locationsAdded++;
            }
            if(crime.getOutcomeStatus() != null && insertStatus(crime.getOutcomeStatus())){
                statusesAdded++;
            }
            if(insertCrime(crime)){
                crimesAdded++;
            }
        }
        logger.info("Uniq streets added " + streetsAdded);
        logger.info("Uniq locations added " + locationsAdded);
        logger.info("Uniq statuses added " + statusesAdded);
        logger.info("Uniq crimes added " + crimesAdded);
    }


    private boolean insertStatus(OutcomeStatus status) throws DaoException{
        if(isStatusAdded(status)){
            return false;
        }
        boolean isStatusAdded = false;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_OUTCOME_STATUS)){
            pst.setString(1,status.getCategory());
            pst.setDate(2,java.sql.Date.valueOf(status.getDate()));
            isStatusAdded = pst.executeUpdate() > 0;
        }catch (SQLException e){
            throw new DaoException("Exception while adding status",e);
        }
        return isStatusAdded;
    }

    private boolean insertCrime(Crime crime) throws DaoException{
        if(isCrimeAdded(crime)){
            return false;
        }
        boolean isStatusNull = crime.getOutcomeStatus() == null;
        boolean isCrimeInsert = false;
        String preparedString = "";
        if(isStatusNull){
            preparedString = INSERT_CRIME_WITHOUT_STATUS;
        }else {
            preparedString = INSERT_CRIME;
        }
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(preparedString)){
            pst.setLong(1,crime.getId());
            pst.setString(2,crime.getCategory());
            pst.setString(3,crime.getLocationType());
            pst.setString(4,crime.getContext());
            pst.setString(5,crime.getPersistentID());
            pst.setString(6,crime.getLocationSubtype());
            pst.setDate(7,java.sql.Date.valueOf(crime.getMonth()));
            pst.setDouble(8,crime.getLocation().getLongitude());
            pst.setDouble(9,crime.getLocation().getLatitude());
            pst.setInt(10,crime.getLocation().getStreet().getId());
            if (!isStatusNull){
                pst.setString(11,crime.getOutcomeStatus().getCategory());
                pst.setDate(12,java.sql.Date.valueOf(crime.getOutcomeStatus().getDate()));
            }
            isCrimeInsert = pst.executeUpdate() > 0;
        }catch (SQLException e){
            throw new DaoException("Exception while adding crime",e);
        }
        return isCrimeInsert;
    }

    private boolean insertLocation(Location location) throws DaoException{
        if(isLocationAdded(location)){
            return false;
        }
        boolean isLocationInsert = false;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_LOCATION)){
            pst.setDouble(1,location.getLongitude());
            pst.setDouble(2,location.getLatitude());
            pst.setLong(3,location.getStreet().getId());
            isLocationInsert = pst.executeUpdate() > 0;
        }catch (SQLException e){
            throw new DaoException("Exception while adding location",e);
        }
        return isLocationInsert;
    }

    private boolean insertStreet(Street street) throws DaoException{
        if(isStreetAdded(street)){
            return false;
        }
        boolean isStreetInsert = false;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_STREET)){
            pst.setLong(1,street.getId());
            pst.setString(2,street.getName());
            isStreetInsert = pst.executeUpdate() > 0;
        }catch (SQLException e){
            throw  new DaoException("Exception while adding street",e);
        }
        return isStreetInsert;
    }

    private boolean isStreetAdded(Street street) throws DaoException{
        boolean isAdded = false;
        ResultSet resultSet = null;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(FIND_STREET)) {
            pst.setInt(1,street.getId());
            resultSet = pst.executeQuery();
            isAdded = resultSet.next();
        } catch (SQLException e) {
            throw new DaoException("Exception while checking if street added", e);
        }
        return isAdded;
    }

    private boolean isStatusAdded(OutcomeStatus status) throws DaoException{
        boolean isAdded = false;
        ResultSet resultSet = null;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(FIND_STATUS)) {
            pst.setString(1, status.getCategory());
            pst.setDate(2, java.sql.Date.valueOf(status.getDate()));
            resultSet = pst.executeQuery();
            isAdded = resultSet.next();
        }catch (SQLException e){
            throw new DaoException("Exception while checking if status added", e);
        }
        return isAdded;
    }

    private boolean isLocationAdded(Location location) throws  DaoException{
        boolean isAdded = false;
        ResultSet resultSet = null;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(FIND_LOCATION)) {
            pst.setDouble(1,location.getLongitude());
            pst.setDouble(2,location.getLatitude());
            resultSet = pst.executeQuery();
            isAdded = resultSet.next();
        } catch (SQLException e) {
            throw new DaoException("Exception while checking if location added",e);
        }
        return isAdded;
    }

    private boolean isCrimeAdded(Crime crime) throws DaoException{
        boolean isAdded = false;
        ResultSet resultSet = null;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(FIND_CRIMES)) {
            pst.setLong(1,crime.getId());
            resultSet = pst.executeQuery();
            isAdded = resultSet.next();
        } catch (SQLException e) {
            throw new DaoException("Exception while checking if crime added",e);
        }
        return isAdded;
    }
}
