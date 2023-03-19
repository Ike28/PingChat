package com.map222.sfmc.socialnetworkfx.service;


import com.map222.sfmc.socialnetworkfx.domain.business.Entity;
import com.map222.sfmc.socialnetworkfx.repository.Repository;

public abstract class Service<ID, E extends Entity<ID>> {
    protected Repository<ID, E> repo;

    /**
     * Creates a service instance
     * @param repo -a repository for storing the entities (may or may not be empty)
     */
    public Service(Repository<ID, E> repo) {
        this.repo = repo;
    }

    /**
     *
     * @param newEntity -the entity to be added
     * @return null, or newEntity if there already exists an entity with newEntity's id
     */
    public E add(E newEntity) {
        E task = repo.add(newEntity);
        return task;
    }

    /**
     *
     * @param entityID -id of the entity to delete
     * @return the deleted entity
     */
    public E delete(ID entityID) {
        E deleted = repo.delete(entityID);
        return deleted;
    }

    /**
     *
     * @return an iterable containing all currently stored entities
     */
    public Iterable<E> getAll() {
        return repo.getAll();
    }

    /**
     *
     * @param entityID -id of the entity to find
     * @return the entity bearing the id entityID
     */
    public E getByID(ID entityID) {
        return repo.getByID(entityID);
    }

    /**
     *
     * @param updatedEntity -the new entity with the id of the old one
     * @return the updated entity
     */
    public E update(E updatedEntity) {
        E task = repo.update(updatedEntity);
        return task;
    }
}
