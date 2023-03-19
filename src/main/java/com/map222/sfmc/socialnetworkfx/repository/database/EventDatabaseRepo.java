package com.map222.sfmc.socialnetworkfx.repository.database;

import com.map222.sfmc.socialnetworkfx.domain.business.Event;
import com.map222.sfmc.socialnetworkfx.domain.utils.ConnectionFactory;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.SQLQueryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventDatabaseRepo extends AbstractDatabaseRepository<UUID, Event> {
    public EventDatabaseRepo(String url, String username, String password, Validator<Event> validator) {
        super(url, username, password, validator);
    }

    public EventDatabaseRepo(String username, String password, Validator<Event> validator) {
        super(username, password, validator);
    }

    @Override
    public Iterable<Event> getAll() {
        Set<Event> events = new HashSet<>();
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();

            String query = "SELECT * FROM events";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {

                events.add(getNextEntity(resultSet));
            }

            return events;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Event getByID(UUID entityID) {
        if(entityID == null)
            throw new InvalidIDException("\nID must not be null.");

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();

            String query = "SELECT * FROM events WHERE id = \'" + entityID + "'";

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
    public Event add(Event newEntity) {
        if(newEntity == null)
            throw new InvalidEntityException("\nEntity must not be null.");
        validator.validate(newEntity);

        if(this.getByID(newEntity.getID()) != null) {
            return newEntity;
        }

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();

            String addCommand = "INSERT INTO events (id, title, event_date) "
                    + " VALUES(\'" + newEntity.getID().toString()
                    +  "',\'" + newEntity.getTitle()
                    + "',\'" + newEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + "')";

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
    public Event delete(UUID entityID) {
        Event deletedEvent = this.getByID(entityID);
        if(deletedEvent == null)
            throw new InvalidIDException("\nNo entity exists with the ID=" + entityID);

        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();

            String query = "DELETE FROM events WHERE id = \'" + entityID.toString() + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            return deletedEvent;
        }
        catch(SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }

    @Override
    public Event update(Event newEntity) {
        return null;
    }

    @Override
    protected Event getNextEntity(ResultSet queryResultSet) {
        try {
            UUID eventID = UUID.fromString(queryResultSet.getString("id"));
            String title = queryResultSet.getString("title");
            LocalTime eventTime = queryResultSet.getTime("event_date").toLocalTime();
            LocalDateTime eventDate = queryResultSet.getDate("event_date").toLocalDate().atTime(eventTime);

            Event newEvent = new Event(title, eventDate);
            newEvent.setID(eventID);

            return newEvent;

        }
        catch (SQLException sqle) {
            String errorMessage = "\nError in reading database:\n" + sqle.getMessage() + "\n--->try again!";
            throw new SQLQueryException(errorMessage);
        }
    }
}
