package com.map222.sfmc.socialnetworkfx.repository.database;

import com.map222.sfmc.socialnetworkfx.domain.utils.ConnectionFactory;
import com.map222.sfmc.socialnetworkfx.domain.utils.DatabaseCredentials;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParticipationDBRepo {
    private String urlDB;
    private String usernameDB;
    private String passwordDB;

    public ParticipationDBRepo(String urlDB, String usernameDB, String passwordDB) {
        this.urlDB = urlDB;
        this.usernameDB = usernameDB;
        this.passwordDB = passwordDB;
    }

    public ParticipationDBRepo() {
        this.urlDB = DatabaseCredentials.getUrl();
        this.usernameDB = DatabaseCredentials.getUsername();
        this.passwordDB = DatabaseCredentials.getPassword();
    }

    private boolean participationExists(String username, UUID eventID) throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        String query = "SELECT * FROM eventparticipation WHERE username = \'" + username + "' " +
                "AND eventid = \'" + eventID.toString() + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next())
            return true;
        return false;
    }

    public List<Pair<UUID, String>> getAll() throws SQLQueryException {
        try {
            List<Pair<UUID, String>> participations = new ArrayList<>();
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM eventparticipation";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                String username = resultSet.getString("username");
                UUID eventID = UUID.fromString(resultSet.getString("eventid"));
                participations.add(new Pair<>(eventID, username));
            }
            return participations;
        }
        catch (SQLException sqle) {
            throw new SQLQueryException(sqle.getMessage());
        }
    }

    public List<Pair<UUID, String>> getAllOfUser(String username) throws SQLQueryException, InvalidIDException {
        try {
            List<Pair<UUID, String>> participations = new ArrayList<>();
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM eventparticipation WHERE username = \'" + username + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                UUID eventID = UUID.fromString(resultSet.getString("eventid"));
                participations.add(new Pair<>(eventID, username));
            }
            return participations;
        }
        catch (SQLException sqle) {
            throw new SQLQueryException(sqle.getMessage());
        }
    }

    public void addEventParticipation(String username, UUID eventID) {
        try {
            if(this.participationExists(username, eventID))
                throw new InvalidIDException("\nYou are already taking part in this event");

            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "INSERT INTO eventparticipation(username, eventid) " +
                    "VALUES(\'" + username + "', " +
                    "\'" + eventID.toString() + "')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }
        catch (SQLException sqle) {
            throw new SQLQueryException(sqle.getMessage());
        }
    }

    public void removeEventParticipation(String username, UUID eventID) {
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "DELETE FROM eventparticipation WHERE username = \'" + username + "' " +
                    "AND eventid = \'" + eventID.toString() + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }
        catch (SQLException sqle) {
            throw new SQLQueryException(sqle.getMessage());
        }
    }
}
