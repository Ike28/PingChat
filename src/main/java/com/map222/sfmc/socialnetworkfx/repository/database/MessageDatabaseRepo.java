package com.map222.sfmc.socialnetworkfx.repository.database;


import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.ConnectionFactory;
import com.map222.sfmc.socialnetworkfx.domain.utils.StringGens;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MessageDatabaseRepo extends AbstractDatabaseRepository<UUID, Message> {
    private UserDatabaseRepo userRepo;

    public MessageDatabaseRepo(String url, String username, String password, Validator<Message> validator, UserDatabaseRepo userRepo) {
        super(url, username, password, validator);
        this.userRepo = userRepo;
    }

    public MessageDatabaseRepo(String username, String password, Validator<Message> validator, UserDatabaseRepo userRepo) {
        super(username, password, validator);
        this.userRepo = userRepo;
    }

    @Override
    public Iterable<Message> getAll() {
        Set<Message> messages = new HashSet<>();
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();

            String query = "SELECT * FROM messages";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {

                messages.add(getNextEntity(resultSet));
            }

            return messages;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    public Iterable<Message> getAll(User user1, User user2) {
        Set<Message> messages = new HashSet<>();
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();

            String query = "(SELECT * FROM messages WHERE sender = \'" + user1.getID()
                    + "' AND receiver = \'" + user2.getID()
                    + "') UNION (SELECT * FROM messages WHERE sender = \'" + user2.getID()
                    + "' AND receiver = \'" + user1.getID() + "')";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {

                messages.add(getNextEntity(resultSet));
            }

            return messages;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Message getByID(UUID entityID) {
        if(entityID == null)
            throw new InvalidIDException("\nID must not be null.");

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();

            String query = "SELECT * FROM messages WHERE id = \'" + entityID + "'";

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
    public Message add(Message newEntity) throws SQLQueryException {
        if(newEntity == null)
            throw new InvalidEntityException("\nEntity must not be null.");
        validator.validate(newEntity);

        if(this.getByID(newEntity.getID()) != null) {
            return newEntity;
        }

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String replyRootArgument;
            if(newEntity.getReplyRoot() == null)
                replyRootArgument = "NULL";
            else replyRootArgument = "\'" + newEntity.getReplyRoot().toString() + "'";

            String addCommand = "INSERT INTO messages (id, sender, receiver, msg, send_date, reply_root) "
                    + " VALUES(\'" + newEntity.getID().toString()
                    +  "',\'" + newEntity.getSender().getID()
                    + "',\'" + newEntity.getReceiver().getID()
                    +  "',\'" + newEntity.getMessage()
                    + "',\'" + newEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + "'," + replyRootArgument
                    + ")";

            PreparedStatement statement = connection.prepareStatement(addCommand);
            statement.execute();
            return null;
        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in writing to database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Message delete(UUID entityID) {

        Message deletedMessage = this.getByID(entityID);
        if(deletedMessage == null)
            throw new InvalidIDException("\nNo entity exists with the ID=" + entityID);

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();

            String query = "DELETE FROM messages WHERE id = \'" + entityID.toString() +"'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return deletedMessage;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Message update(Message newEntity) {
        return null;
    }

    @Override
    protected Message getNextEntity(ResultSet queryResultSet) {
        try {
            UUID messageID = UUID.fromString(queryResultSet.getString("id"));
            User sender = this.userRepo.getByID(queryResultSet.getString("sender"));
            User receiver = this.userRepo.getByID(queryResultSet.getString("receiver"));
            String message = queryResultSet.getString("msg");

            LocalTime messageTime = queryResultSet.getTime("send_date").toLocalTime();
            LocalDateTime messageDate = queryResultSet.getDate("send_date").toLocalDate().atTime(messageTime);

            String reply_root_query = queryResultSet.getString("reply_root");
            UUID replyRoot;
            if(reply_root_query == null)
                replyRoot = null;
            else replyRoot = UUID.fromString(reply_root_query);

            Message newMessage = new Message(sender, receiver, message, messageDate);
            newMessage.setID(messageID);
            newMessage.setReplyRoot(replyRoot);

            return newMessage;

        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }
}
