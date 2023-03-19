package com.map222.sfmc.socialnetworkfx.repository.database;


import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.utils.ConnectionFactory;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.utils.StringGens;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipDatabaseRepo extends AbstractDatabaseRepository<Pair<String, String>, Friendship> {

    public FriendshipDatabaseRepo(String url, String username, String password, Validator<Friendship> validator) {
        super(url, username, password, validator);
    }

    public FriendshipDatabaseRepo(String username, String password, Validator<Friendship> validator) {
        super(username, password, validator);
    }

    @Override
    protected Friendship getNextEntity(ResultSet queryResultSet) throws SQLQueryException {
        try {
            String username1 = queryResultSet.getString("username_user1");
            String username2 = queryResultSet.getString("username_friend");

            LocalDateTime friendshipDate;
            if (queryResultSet.getDate("friendship_date") == null)
                friendshipDate = LocalDateTime.now();
            else {
                friendshipDate = queryResultSet.getDate("friendship_date").toLocalDate().atStartOfDay();
            }
            Friendship newFriendship = new Friendship(username1, username2, friendshipDate);
            return newFriendship;
        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Iterable<Friendship> getAll() throws SQLQueryException {
        Set<Friendship> friendships = new HashSet<>();
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {

                friendships.add(getNextEntity(resultSet));
            }
            return friendships;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Friendship getByID(Pair<String, String> friendshipID) throws InvalidIDException, SQLQueryException {
        if(friendshipID==null)
            throw new InvalidIDException("\nID must not be null.");
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM friendships WHERE username_user1 = \'" + friendshipID.getFirstOfPair() + "'" +
                    " AND username_friend = \'" + friendshipID.getSecondOfPair() + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next())
                return null;

            return getNextEntity(resultSet);
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Friendship add(Friendship newFriendship) throws SQLQueryException, InvalidEntityException, ValidationException {
        if(newFriendship == null)
            throw new InvalidEntityException("\nEntity must not be null.");
        validator.validate(newFriendship);
        if(this.getByID(newFriendship.getID()) != null) {
            return newFriendship;
        }

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String newUser1 = newFriendship.getID().getFirstOfPair();
            String newUser2 = newFriendship.getID().getSecondOfPair();

            /*String chat_tablename = StringGens.getChatTablename(newFriendship);

            String createQuery = "CREATE TABLE IF NOT EXISTS " + chat_tablename + " (" +
                    "   id varchar PRIMARY KEY," +
                    "   sender varchar NOT NULL," +
                    "   receiver varchar NOT NULL," +
                    "   msg varchar NOT NULL," +
                    "   send_date timestamp," +
                    "   reply_root varchar " +
                    ");";

            statement.execute();*/

            String date = newFriendship.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String query = "INSERT INTO friendships(username_user1, username_friend, friendship_date)" +
                    "       VALUES (\'" + newUser1 +
                    "',\'" + newUser2 +
                    "',\'" + date +
                    "')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return null;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in writing to database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Friendship delete(Pair<String, String> friendshipID) throws InvalidIDException, SQLQueryException{
        if(friendshipID == null)
            throw new InvalidIDException("\nCannot delete entity with ID=NULL");
        Friendship deletedFriendship = this.getByID(friendshipID);
        if(deletedFriendship == null)
            throw new InvalidIDException("\nNo entity exists with the ID=" + friendshipID);
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "DELETE FROM friendships WHERE username_user1 = \'" + friendshipID.getFirstOfPair() + "'" +
                    " AND username_friend = \'" + friendshipID.getSecondOfPair() + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return deletedFriendship;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Friendship update(Friendship newFriendship) {
        return null;
    }

    public List<String> getUserFriendList(String userID) throws InvalidIDException {
        if(username == null)
            throw new InvalidIDException("\nCannot return friends list for NULL username");

        List<String> userlist = new ArrayList<>();

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "(SELECT username_friend AS name FROM friendships WHERE username_user1 = \'" + userID + "')" +
                    " UNION (SELECT username_user1 AS name FROM friendships WHERE username_friend = \'" + userID + "')";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                userlist.add(resultSet.getString("name"));
            }

            return userlist;

        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

}
