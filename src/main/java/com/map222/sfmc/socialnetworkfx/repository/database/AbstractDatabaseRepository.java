package com.map222.sfmc.socialnetworkfx.repository.database;


import com.map222.sfmc.socialnetworkfx.domain.business.Entity;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.repository.Repository;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;

import java.sql.ResultSet;

/**
 * Abstract base class for DB-based repository classes
 * @param <ID> -id class for Entity-derived E class
 * @param <E> -Entity-derived class
 */
public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected String url;
    protected String username;
    protected String password;
    protected Validator<E> validator;

    /**
     * Creates a repository instance
     * @param url -URL of the database to access
     * @param username -username for the database connection
     * @param password -password for the database connection
     * @param validator -Validator object for chosen entity class
     */
    protected AbstractDatabaseRepository(String url, String username, String password, Validator<E> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * Creates a repository instance for the default local PostgreSQL server
     * @param username -username for the database connection
     * @param password -password for the database connection
     * @param validator -Validator object for chosen entity class
     */
    protected AbstractDatabaseRepository(String username, String password, Validator<E> validator) {
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.url = "jdbc:postgresql://localhost:5432/socialNetwork";
    }

    /**
     * Fetches all existing objects in the database
     * @return an iterable containing the objects
     */
    @Override
    public abstract Iterable<E> getAll();

    /**
     * Fetches an object from the database based on its ID
     * @param entityID -ID of the desired object
     * @return found object, or null if no result was achieved
     */
    @Override
    public abstract E getByID(ID entityID);

    /**
     * Adds a new valid object to the database
     * @param newEntity -new object to add
     * @return null if the add was successful, otherwise newEntity if an object with its ID already exists
     * @throws ValidationException if the object did not pass validation
     */
    @Override
    public abstract E add(E newEntity);

    /**
     * Deletes an object from the database based on its ID
     * @param entityID -ID of the object to delete
     * @return the deleted object
     * @throws InvalidIDException if no object exists with the specified ID
     */
    @Override
    public abstract E delete(ID entityID);

    /**
     * Updates an object in the database, identified by an ID
     * @param newEntity -new entity having the same ID as the one to update
     * @return newEntity if the update was successful
     * @throws InvalidIDException if no object exists with newEntity's ID
     */
    @Override
    public abstract E update(E newEntity);

    /**
     * Creates an object from a result row provided by an executed query
     * @param queryResultSet -result set to extract the first object from
     * @return the new object
     */
    protected abstract E getNextEntity(ResultSet queryResultSet);
}
