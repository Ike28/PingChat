package com.map222.sfmc.socialnetworkfx.repository.file;


import com.map222.sfmc.socialnetworkfx.domain.relational.Friendship;
import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.InvalidIDException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Pair<String, String>, Friendship> {
    public FriendshipFileRepository(String filename, Validator<Friendship> validator) {
        super(filename, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        LocalDateTime date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        date = LocalDateTime.parse(attributes.get(2), formatter);
        Friendship friendship = new Friendship(attributes.get(0), attributes.get(1), date);

        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        String date = entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return entity.getID().getFirstOfPair() + ";" + entity.getID().getSecondOfPair() + ";" + date;
    }

    /**
     * Hard-deletes a friendship
     * @param id -the id of the entity to delete
     *      id must be not null
     * @return the deleted friendship
     * @throws InvalidIDException
     *          if id is null
     */
    @Override
    public Friendship delete(Pair<String, String> id) throws InvalidIDException {
        if(id == null)
            throw new InvalidIDException("\nCannot delete entity with ID=NULL");
        if(!entities.containsKey(id))
            throw new InvalidIDException("\nNo entity exists with the ID=" + id.toString());
        Friendship entity = entities.get(id);
        entities.remove(id);
        writeAllToFile();
        return entity;
    }
}
