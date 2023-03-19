package com.map222.sfmc.socialnetworkfx.repository.file;


import com.map222.sfmc.socialnetworkfx.domain.business.Entity;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.DeletedEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.repository.memory.RuntimeRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends RuntimeRepository<ID, E> {
    String filename;

    public AbstractFileRepository(String filename, Validator<E> validator) {
        super(validator);
        this.filename = filename;
        loadData();
    }

    /**
     * Loads the data from the filename
     */
    protected void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            List<String> list = new ArrayList<>();
            while ((line = br.readLine()) != null && !line.equals("")) {
                list.add(line);
            }
            list.forEach(ln ->
            {

                    E entity = extractEntity(Arrays.asList(ln.split(";")));
                    super.add(entity);

            });
        }
        catch (IOException ie) {
            System.out.println("\nERROR IN READING " + filename + "\n");
            ie.printStackTrace();
        }
        catch(NullPointerException ne) {
            System.out.println("\nWarning: no users in " + filename);
        }
    }

    /**
     * Appends a new entity to file
     * @param entity -the entity to append
     */
    protected void writeToFile(E entity) {
        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(filename, true));
            output.append(createEntityAsString(entity)).append("\n");
            output.close();
        }
        catch (IOException ie) {
            System.out.println("\nERROR IN WRITING TO " + filename + "\n");
            ie.printStackTrace();
        }
    }

    /**
     * Overwrites the file with all current entities
     */
    protected void writeAllToFile() {
        try {
            Writer output = new BufferedWriter(new FileWriter(filename, false));
            for(E entity: entities.values())
                output.append(createEntityAsString(entity)).append("\n");
            output.close();
        }
        catch (IOException ie) {
            System.out.println("\nERROR IN WRITING TO " + filename + "\n");
            ie.printStackTrace();
        }
    }

    /**
     * Creates a new entity from String attributes
     * @param attributes -a list containing the entity's attributes as valid parsable strings
     * @return the new entity
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * Serializes an entity to a string
     * @param entity -the entity to serialize
     * @return a string representing the entity, which can be parsed back via extractEntity
     */
    protected abstract String createEntityAsString(E entity);

    /**
     *
     * @param entity -the entity to be added
     *         entity must be not null
     * @return null, or entity if there already exists one with its id
     * @throws InvalidEntityException
     *          if entity is null
     */
    @Override
    public E add(E entity) {
        if(entity == null)
            throw new InvalidEntityException("\nEntity must not be null.");
        validator.validate(entity);
        if(entities.get(entity.getID()) != null) {
            return entity;
        }
        else {
            entities.put(entity.getID(), entity);
            writeToFile(entity);
        }
        return null;
    }

    /**
     *
     * @param id -the id of the entity to delete
     *      id must be not null
     * @return the deleted entity
     * @throws InvalidIDException
     *          if id is null or there is no entity with such id
     * @throws DeletedEntityException
     *          if the entity bearing the given id has already been deleted
     */
    @Override
    public E delete(ID id) throws InvalidIDException, DeletedEntityException {
        if(id == null)
            throw new InvalidIDException("\nCannot delete entity with ID=NULL");
        if(!entities.containsKey(id))
            throw new InvalidIDException("\nNo entity exists with the ID=" + id.toString());
        if(entities.get(id).isDeleted())
            throw new DeletedEntityException("\nEntity with the ID=" + id.toString() + " has already been deleted.");
        E entity = entities.get(id);
        entity.delete();
        entities.put(entity.getID(), entity);
        writeAllToFile();
        return entity;
    }

    /**
     *
     * @param entity -the new entity with id matching the old one's id
     *          entity must not be null
     * @return the updated entity
     * @throws InvalidEntityException
     *          if entity is null
     * @throws InvalidIDException
     *          if there is no existing entity with entity's id
     * @throws DeletedEntityException
     *          if the old entity has been deleted
     */
    @Override
    public E update(E entity) {
        if(entity == null)
            throw new InvalidEntityException("\nEntity must not be null.");
        validator.validate(entity);

        if(getByID(entity.getID()) == null)
            throw new InvalidIDException("\nNo entity exists with the ID=" + entity.getID().toString());

        E oldEntity = entities.get(entity.getID());
        if(oldEntity != null) {
            if(!oldEntity.isDeleted()) {
                entities.put(entity.getID(), entity);
                writeAllToFile();
                return null;
            }
            else throw new DeletedEntityException("\nAttempting to update deleted entity.");
        }

        return entity;
    }
}
