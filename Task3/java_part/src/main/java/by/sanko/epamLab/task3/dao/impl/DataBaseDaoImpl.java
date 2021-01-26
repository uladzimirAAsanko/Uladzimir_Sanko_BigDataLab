package by.sanko.epamLab.task3.dao.impl;

import by.sanko.epamLab.task3.dao.Dao;
import by.sanko.epamLab.task3.dao.pool.ConnectionPool;
import by.sanko.epamLab.task3.entity.Crime;
import by.sanko.epamLab.task3.entity.Location;
import by.sanko.epamLab.task3.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DataBaseDaoImpl implements Dao {
    private static final String INSERT_STREET = "INSERT INTO streets VALUES(?,?)";
    private static final String INSERT_LOCATION = "INSERT INTO locations VALUES (DEFAULT,?,?,?);";
    private static final String INSERT_OUTCOME_STATUS = "INSERT INTO outcome_statuses VALUES (DEFAULT,?,?);";
    private static final String INSERT_CRIME = "INSERT INTO crimes VALUES(?,?,?,?,?,?,?,(SELECT location_id FROM locations" +
            " WHERE longitude=? AND latitude = ? AND street_id =? LIMIT 1),(SELECT outcome_status_id FROM outcome_statuses " +
            "WHERE outcome_statuses.category=? AND date=? LIMIT 1));";
    private static final String INSERT_CRIME_WITHOUT_STATUS = "INSERT INTO crimes VALUES(?,?,?,?,?," +
            "?,?,(SELECT location_id FROM locations WHERE longitude=? AND latitude = ?" +
            "AND street_id =? LIMIT 1 ),?);";
    private static int crimesAdded = 0;
    private static int statusesAdded = 0;
    private static int streetsAdded = 0;
    private static int locationsAdded = 0;



    @Override
    public boolean addInformation(List<Crime> crimes) throws DaoException {
        crimesAdded = 0;
        statusesAdded = 0;
        streetsAdded = 0;
        locationsAdded = 0;
        try{
            for (Crime crime: crimes) {
                insertInformation(crime);
            }
        }catch (SQLException e){
            throw new DaoException("Exception while adding in database" + e.getMessage());
        }
        System.out.println("Uniq streets added " + streetsAdded);
        System.out.println("Uniq locations added " + locationsAdded);
        System.out.println("Uniq statuses added " + statusesAdded);
        System.out.println("Uniq crimes added " + crimesAdded);
        return true;
    }

    private boolean insertInformation (Crime crime) throws SQLException{
        Location.Street street = crime.getLocation().getStreet();
        Crime.OutcomeStatus status = crime.getOutcomeStatus();
        boolean isStreetAdded = false;
        boolean isLocationAdded = false;
        boolean isCrimeAdded = false;
        boolean isStatusAdded = false;
        boolean answer = false;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_STREET)){
            pst.setLong(1,street.getId());
            pst.setString(2,street.getName());
            isStreetAdded = pst.executeUpdate() > 0;
        }catch (SQLException e){

        }
        if(isStreetAdded){
            streetsAdded++;
        }
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_LOCATION)){
            Location location = crime.getLocation();
            pst.setDouble(1,location.getLongitude());
            pst.setDouble(2,location.getLatitude());
            pst.setLong(3,street.getId());
            isLocationAdded = pst.executeUpdate() > 0;
        }catch (SQLException e){

        }
        if(isLocationAdded){
            locationsAdded++;
        }
        if(crime.getOutcomeStatus().getDate() == null){
            isCrimeAdded = insertCrimeWithoutStatus(crime);
        }else{
            isStatusAdded = insertStatus(status);
            isCrimeAdded = insertCrimeWithStatus(crime);
        }
        if (isCrimeAdded){
            crimesAdded++;
            answer = true;
        }
        if(isStatusAdded){
            statusesAdded++;
        }

        return answer;
    }

    private boolean insertStatus(Crime.OutcomeStatus status) throws SQLException{
        boolean isStatusAdded = false;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_OUTCOME_STATUS)){
            pst.setString(1,status.getCategory());
            pst.setDate(2,java.sql.Date.valueOf(status.getDate()));
            isStatusAdded = pst.executeUpdate() > 0;
        }catch (SQLException e){

        }
        return isStatusAdded;
    }

    private boolean insertCrimeWithStatus(Crime crime) throws SQLException{
        boolean isCrimeAdded = false;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_CRIME)){
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
            pst.setString(11,crime.getOutcomeStatus().getCategory());
            pst.setDate(12,java.sql.Date.valueOf(crime.getOutcomeStatus().getDate()));
            isCrimeAdded = pst.executeUpdate() > 0;
        }catch (SQLException e){

        }
        return isCrimeAdded;
    }

    private boolean insertCrimeWithoutStatus(Crime crime) throws SQLException{
        boolean isCrimeAdded = false;
        try(Connection connection  = ConnectionPool.getConnection();
            PreparedStatement pst = connection.prepareStatement(INSERT_CRIME_WITHOUT_STATUS)){
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
            pst.setInt(11,-1);
            isCrimeAdded = pst.executeUpdate() > 0;
        }catch (SQLException e){

        }
        return isCrimeAdded;
    }
}
