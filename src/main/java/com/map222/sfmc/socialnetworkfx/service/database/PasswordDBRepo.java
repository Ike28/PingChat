package com.map222.sfmc.socialnetworkfx.service.database;

import com.map222.sfmc.socialnetworkfx.domain.utils.ConnectionFactory;
import com.map222.sfmc.socialnetworkfx.domain.utils.DatabaseCredentials;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;

import java.sql.*;
import java.util.Objects;

public class PasswordDBRepo {
    private String urlDB;
    private String usernameDB;
    private String passwordDB;

    public PasswordDBRepo(String urlDB, String usernameDB, String passwordDB) {
        this.urlDB = urlDB;
        this.usernameDB = usernameDB;
        this.passwordDB = passwordDB;
    }

    public PasswordDBRepo() {
        this.urlDB = DatabaseCredentials.getUrl();
        this.usernameDB = DatabaseCredentials.getUsername();
        this.passwordDB = DatabaseCredentials.getPassword();
    }

    public Pair<String, String> getByID(String usernameHash) throws SQLQueryException {
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM passwords WHERE username = \'" + usernameHash + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String passHash = resultSet.getString("password");
                return new Pair<>(usernameHash, passHash);
            }
            else {
                return null;
            }

        }
        catch (SQLException sqle) {
            throw new SQLQueryException(sqle.getMessage());
        }
    }

    public void add(String usernameHash, String passwordHash) throws SQLQueryException, InvalidIDException {
        try {
            if(this.getByID(usernameHash) != null)
                throw new InvalidIDException("\nThis username is already taken");

            Connection connection = ConnectionFactory.getInstance().getConnection();
            String addCommand = "INSERT INTO passwords(username, password) " +
                    "VALUES (\'" + usernameHash + "', \'" + passwordHash + "');";
            PreparedStatement statement = connection.prepareStatement(addCommand);
            statement.execute();
        }
        catch (SQLException sqle) {
            throw new SQLQueryException(sqle.getMessage());
        }
    }

    public boolean matchPass(String usernameHash, String passwordHash) throws SQLQueryException, InvalidIDException {
        Pair<String, String> credentials = this.getByID(usernameHash);
        if(credentials == null)
            throw new InvalidIDException("\nInvalid username and password combination");

        if(Objects.equals(credentials.getSecondOfPair(), passwordHash))
            return true;
        return false;
    }

    public void changePassword(String usernameHash, String passwordHash, String newPasswordHash) throws InvalidIDException {
        if(!matchPass(usernameHash, passwordHash))
            throw new InvalidIDException("\nInvalid username and password combination");

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String updateCommand = "UPDATE passwords SET password = \'" + newPasswordHash + "' WHERE username = \'" + usernameHash + "'";
            PreparedStatement statement = connection.prepareStatement(updateCommand);
            statement.execute();
        }
        catch (SQLException sqle) {
            throw new SQLQueryException(sqle.getMessage());
        }
    }
}
