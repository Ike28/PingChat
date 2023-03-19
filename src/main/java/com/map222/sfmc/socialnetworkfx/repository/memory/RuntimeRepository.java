package com.map222.sfmc.socialnetworkfx.repository.memory;

import com.map222.sfmc.socialnetworkfx.domain.business.Entity;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.DeletedEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidEntityException;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;
import com.map222.sfmc.socialnetworkfx.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RuntimeRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected Validator<E> validator;
    protected Map<ID, E> entities;

    public RuntimeRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity, or null if there is no entity with the given id
     * @throws InvalidIDException
     *          if id is null
     */
    @Override
    public E getByID(ID id) {
        if(id==null)
            throw new InvalidIDException("\nID must not be null.");
        return entities.get(id);
    }

    /**
     *
     * @return an iterable containing all active entities
     */
    @Override
    public Iterable<E> getAll() {
        Map<ID, E> filtered = entities.entrySet().stream()
                                .filter(entry -> !entry.getValue().isDeleted())
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return filtered.values();
    }

    /**
     *
     * @param deletedItems -if true, the return value will also contain deleted entities
     * @return an iterable containing all entities
     */
    public Iterable<E> getAll(boolean deletedItems) {
        if(!deletedItems)
            return getAll();
        else return entities.values();
    }

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
        else entities.put(entity.getID(), entity);
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
    public E delete(ID id) {
        if(id == null)
            throw new InvalidIDException("\nCannot delete entity with ID=NULL");
        if(!entities.containsKey(id))
            throw new InvalidIDException("\nNo entity exists with the ID=" + id.toString());
        if(entities.get(id).isDeleted())
            throw new DeletedEntityException("\nEntity with the ID=" + id.toString() + " has already been deleted.");
        E entity = entities.get(id);
        entity.delete();
        entities.put(entity.getID(), entity);
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
                return null;
            }
            else throw new DeletedEntityException("\nAttempting to update deleted entity.");
        }

        return entity;
    }
}
