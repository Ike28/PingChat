package com.map222.sfmc.socialnetworkfx.repository.database;


import com.map222.sfmc.socialnetworkfx.domain.business.Request;
import com.map222.sfmc.socialnetworkfx.domain.utils.ConnectionFactory;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestDatabaseRepo extends AbstractDatabaseRepository<Pair<String, String>, Request>{

    public RequestDatabaseRepo(String url, String username, String password, Validator<Request> validator) {
        super(url, username, password, validator);
    }

    public RequestDatabaseRepo(String username, String password, Validator<Request> validator) {
        super(username, password, validator);
    }

    public Request getActiveRequest(String sender, String receiver) throws SQLQueryException {
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM requests WHERE username_user1 = \'" + sender + "' " +
                    "AND username_friend = \'" + receiver + "' AND status = 'pending'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next())
                return null;

            return getNextEntity(resultSet);
        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    protected Request getNextEntity(ResultSet queryResultSet) throws SQLQueryException {
        try {
            String username1 = queryResultSet.getString("username_user1");
            String username2 = queryResultSet.getString("username_friend");

            String status;
            if (queryResultSet.getString("status") == null)
                status = "pending";
            else {
                status = queryResultSet.getString("status").toString();
            }
            Request newRequest = new Request(username1, username2, status);
            return newRequest;
        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Iterable<Request> getAll() throws SQLQueryException{
        Set<Request> requests = new HashSet<>();
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM requests");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {

                requests.add(getNextEntity(resultSet));
            }
            return requests;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Request getByID(Pair<String, String> requestID) throws InvalidIDException, SQLQueryException {
        if(requestID==null)
            throw new InvalidIDException("\nID must not be null.");
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM requests WHERE username_user1 = \'" + requestID.getFirstOfPair() + "'" +
                    " AND username_friend = \'" + requestID.getSecondOfPair() + "' AND status = 'pending'";
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
    public Request add(Request newRequest) throws SQLQueryException, InvalidEntityException, ValidationException {
        if(newRequest == null)
            throw new InvalidEntityException("\nEntity must not be null.");
        validator.validate(newRequest);
        if(this.getByID(newRequest.getID()) != null) {
            return newRequest;
        }

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String newUser1 = newRequest.getID().getFirstOfPair();
            String newUser2 = newRequest.getID().getSecondOfPair();


            String status = newRequest.get_status();
            String query = "INSERT INTO requests(username_user1, username_friend, status)" +
                    "       VALUES (\'" + newUser1 +
                    "',\'" + newUser2 +
                    "',\'" + status +
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
    public Request delete(Pair<String, String> requestID) throws InvalidIDException, SQLQueryException{
        if(requestID == null)
            throw new InvalidIDException("\nCannot delete entity with ID=NULL");
        Request deletedRequests = this.getByID(requestID);
        if(deletedRequests == null)
            throw new InvalidIDException("\nNo entity exists with the ID=" + requestID);
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "DELETE FROM requests WHERE username_user1 = \'" + requestID.getFirstOfPair() + "'" +
                    " AND username_friend = \'" + requestID.getSecondOfPair() + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return deletedRequests;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }


    @Override
    public Request update(Request newRequest) throws InvalidEntityException, InvalidIDException, SQLQueryException{
        if(newRequest == null)
            throw new InvalidEntityException("\nEntity must not be null");
        validator.validate(newRequest);

        Request oldRequest = this.getByID(newRequest.getID());
        if(oldRequest == null)
            throw new InvalidIDException("\nNo entity exists with the ID=" +newRequest.getID());
        try{
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "UPDATE requests" +
                    " SET status = \'" + newRequest.get_status() +
                    "' Where username_user1 = \'" + newRequest.getID().getFirstOfPair() +
                    "' AND username_friend = \'" +newRequest.getID().getSecondOfPair() + "'";
                    ;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return newRequest;

        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }


    public List<String> getUserRequestList(String userID) throws InvalidIDException {
        if(username == null)
            throw new InvalidIDException("\nCannot return requests list for NULL username");

        List<String> userlist = new ArrayList<>();

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "(SELECT username_friend AS name FROM requests WHERE username_user1 = \'" + userID + "')" +
                    " UNION (SELECT username_user1 AS name FROM requests WHERE username_friend = \'" + userID + "')";
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
