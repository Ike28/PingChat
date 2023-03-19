package com.map222.sfmc.socialnetworkfx.repository.database;


import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.ConnectionFactory;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDatabaseRepo extends AbstractDatabaseRepository<String, User> {
    private FriendshipDatabaseRepo friendshipRepo;

    public UserDatabaseRepo(String url, String username, String password, Validator<User> validator, FriendshipDatabaseRepo friendshipRepo) {
        super(url, username, password, validator);
        this.friendshipRepo = friendshipRepo;
    }

    public UserDatabaseRepo(String username, String password, Validator<User> validator, FriendshipDatabaseRepo friendshipRepo) {
        super(username, password, validator);
        this.friendshipRepo = friendshipRepo;
    }

    @Override
    protected User getNextEntity(ResultSet queryResultSet) throws SQLQueryException {
        try {
            String username = queryResultSet.getString("username");
            String firstname = queryResultSet.getString("firstname");
            String lastname = queryResultSet.getString("lastname");

            User newUser = new User(firstname, lastname);
            newUser.setID(username);
            newUser.amendDelete();
            return newUser;
        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Iterable<User> getAll() throws SQLQueryException{
        Set<User> users = new HashSet<>();
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {

                User newUser = getNextEntity(resultSet);
                newUser.setFriends(friendshipRepo.getUserFriendList(newUser.getID()));
                users.add(newUser);
            }
            return users;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public User getByID(String userID) throws InvalidIDException, SQLQueryException {
        if(userID==null)
            throw new InvalidIDException("\nID must not be null.");
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM users WHERE username = \'" + userID + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next())
                return null;

            User newUser = getNextEntity(resultSet);
            newUser.setFriends(friendshipRepo.getUserFriendList(newUser.getID()));

            return newUser;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public User add(User newUser) throws SQLQueryException, InvalidEntityException, ValidationException {
        if(newUser == null)
            throw new InvalidEntityException("\nEntity must not be null.");

        validator.validate(newUser);

        if(this.getByID(newUser.getID()) != null) {
            return newUser;
        }

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String newUsername = newUser.getID();
            String newFirstname = newUser.getFirstName();
            String newLastname = newUser.getLastName();
            String query = "INSERT INTO users(username, firstname, lastname)" +
                    "       VALUES (\'" + newUsername +
                            "',\'" + newFirstname +
                            "',\'" + newLastname +
                            "')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return null;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public User delete(String userID) throws InvalidIDException, SQLQueryException {
        if(userID == null)
            throw new InvalidIDException("\nCannot delete entity with ID=NULL");
        User deletedUser = this.getByID(userID);
        if(deletedUser == null)
            throw new InvalidIDException("\nNo entity exists with the ID=" + userID);
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "DELETE FROM users WHERE username = \'" + userID + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return deletedUser;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public User update(User newUser) throws InvalidEntityException, InvalidIDException, SQLQueryException {
        if(newUser == null)
            throw new InvalidEntityException("\nEntity must not be null.");
        validator.validate(newUser);

        User oldUser = this.getByID(newUser.getID());
        if(oldUser == null)
            throw new InvalidIDException("\nNo entity exists with the ID=" + newUser.getID());
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "UPDATE users" +
                    "       SET firstname = \'" + newUser.getFirstName() +
                            "', lastname = \'" + newUser.getLastName() +
                            "' WHERE username = \'" + newUser.getID() + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return newUser;

        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

}
