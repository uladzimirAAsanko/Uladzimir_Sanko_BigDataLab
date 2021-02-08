package by.sanko.epamLab.task3.dao.impl;

import by.sanko.epamLab.task3.dao.Dao;
import by.sanko.epamLab.task3.dao.pool.ConnectionPool;
import by.sanko.epamLab.task3.entity.*;
import by.sanko.epamLab.task3.exception.DaoException;
import by.sanko.epamLab.task3.service.downloader.impl.HttpDownloaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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
    private static final String INSERT_STOP_AND_SEARCH = "INSERT INTO crime_api.stop_and_searches VALUES (DEFAULT,?,?,?" +
            ",?,?,?,?,?,?,?,?,?,?,?, (SELECT location_id FROM crime_api.locations WHERE latitude=? AND longitude=? AND " +
            "street_id=?), (SELECT force_id FROM crime_api.forces WHERE name=? AND id=?));";
    private static final String INSERT_STOP_AND_SEARCH_WITHOUT_FORCE = "INSERT INTO crime_api.stop_and_searches VALUES " +
            "(DEFAULT,?,?,?,?,?,?,?,?,?,?,?,?,?,?, (SELECT location_id FROM crime_api.locations WHERE latitude=? AND longitude=? AND street_id=?), null);";
    private static final String INSERT_STOP_AND_SEARCH_WITHOUT_LOCATION = "INSERT INTO crime_api.stop_and_searches VALUES (DEFAULT,?,?,?,?,?,?,?,?,?,?,?,?,?,?, null, null);";
    private static final String FIND_FORCE = "SELECT * FROM crime_api.forces WHERE id=?;";
    private static final String INSERT_FORCE = "INSERT INTO crime_api.forces VALUES (DEFAULT,?,?);";

    @Override
    public void addCrimes(List<Crime> crimes) throws DaoException {
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

    @Override
    public void addStopAndSearch(List<StopAndSearch> searches) throws DaoException {
        int locationsAdded = 0;
        int SaSAdded = 0;
        int forcesAdded = 0;
        int streetsAdded = 0;
        for(StopAndSearch SaS : searches){
            if(SaS != null) {
                if (SaS.getLocation() != null && SaS.getLocation().getStreet() != null && insertStreet(SaS.getLocation().getStreet())) {
                    streetsAdded++;
                }
                if (SaS.getOutcomeObject() != null && insertForce(SaS.getOutcomeObject())) {
                    forcesAdded++;
                }
                if (SaS.getLocation() != null && insertLocation(SaS.getLocation())) {
                    locationsAdded++;
                }
                if (insertSaS(SaS)) {
                    SaSAdded++;
                }
            }
        }
        logger.info("Uniq streets added " + streetsAdded);
        logger.info("Uniq  forces added " + forcesAdded);
        logger.info("Uniq locations added " + locationsAdded);
        logger.info("Uniq SaSes added " + SaSAdded);
    }

    private boolean insertForce(Force force) throws DaoException{
        if(isForceAdded(force)){
            return false;
        }
        boolean isInsert = false;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_FORCE)){
            pst.setString(1, force.getId());
            pst.setString(2, force.getName());
            isInsert = pst.executeUpdate() > 0;
        } catch (SQLException throwables) {
            logger.error("Exception while checking if force added" + throwables.getMessage());
        }
        return isInsert;
    }

    private boolean insertSaS(StopAndSearch stopAndSearch){
        boolean isSasAdded = false;
        String querry = "";
        if(stopAndSearch.getOutcomeObject() != null){
            querry = INSERT_STOP_AND_SEARCH;
        }else{
            querry = INSERT_STOP_AND_SEARCH_WITHOUT_FORCE;
        }
        if(stopAndSearch.getLocation() == null){
            querry = INSERT_STOP_AND_SEARCH_WITHOUT_LOCATION;
        }
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(querry)){
            pst.setString(1,stopAndSearch.getType());
            if(stopAndSearch.getInvolvedPerson() == null){
                pst.setNull(2, Types.BOOLEAN);
            }else{
                pst.setBoolean(2,stopAndSearch.getInvolvedPerson());
            }
            if(stopAndSearch.getDateTime() == null){
                pst.setNull(3,Types.DATE);
            }else{
                pst.setDate(3, java.sql.Date.valueOf(stopAndSearch.getDateTime().toLocalDate()));
            }
            if(stopAndSearch.getOperation() == null){
                pst.setNull(4, Types.BOOLEAN);
            }else{
                pst.setBoolean(4,stopAndSearch.getOperation());
            }
            pst.setString(5,stopAndSearch.getOperationName());
            pst.setString(6,stopAndSearch.getGender());
            pst.setString(7,stopAndSearch.getAgeRange());
            pst.setString(8,stopAndSearch.getSelfDefinedEthnicity());
            pst.setString(9,stopAndSearch.getOfficerDefinedEthnicity());
            pst.setString(10,stopAndSearch.getLegislation());
            pst.setString(11,stopAndSearch.getObjectOfSearch());
            pst.setString(12,stopAndSearch.getOutcome());
            if(stopAndSearch.getOutcomeLinkedToObjectOfSearch() == null){
                pst.setNull(13, Types.BOOLEAN);
            }else{
                pst.setBoolean(13,stopAndSearch.getOutcomeLinkedToObjectOfSearch());
            }
            if(stopAndSearch.getRemovalOfMoreThanOuterClothing() == null){
                pst.setNull(14, Types.BOOLEAN);
            }else{
                pst.setBoolean(14,stopAndSearch.getRemovalOfMoreThanOuterClothing());
            }
            if(stopAndSearch.getLocation() != null) {
                pst.setDouble(15, stopAndSearch.getLocation().getLatitude());
                pst.setDouble(16, stopAndSearch.getLocation().getLongitude());
                pst.setInt(17, stopAndSearch.getLocation().getStreet().getId());
                if (stopAndSearch.getOutcomeObject() != null) {
                    pst.setString(18, stopAndSearch.getOutcomeObject().getName());
                    pst.setString(19, stopAndSearch.getOutcomeObject().getId());
                }
            }
            isSasAdded = pst.executeUpdate() > 0;
        }catch (SQLException e){
            logger.error("Error while adding StopAndSearch");
        }
        return isSasAdded;
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
            logger.error("Error while adding StopAndSearch" + e.getMessage());
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

    private boolean isForceAdded(Force force) throws DaoException{
        boolean isAdded = false;
        ResultSet resultSet = null;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(FIND_FORCE)) {
            pst.setString(1,force.getId());
            resultSet = pst.executeQuery();
            isAdded = resultSet.next();
        } catch (SQLException e) {
            logger.error("Exception while checking if force added" + e.getMessage());
        }
        return isAdded;
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
