package com.map222.sfmc.socialnetworkfx.service.database;

import com.map222.sfmc.socialnetworkfx.domain.business.Event;
import com.map222.sfmc.socialnetworkfx.domain.utils.DatabaseCredentials;
import com.map222.sfmc.socialnetworkfx.domain.validators.EventValidator;
import com.map222.sfmc.socialnetworkfx.repository.database.EventDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.service.Service;

import java.util.UUID;

public class EventDBService extends Service<UUID, Event> {
    public EventDBService(EventDatabaseRepo repo) {
        super(repo);
    }

    public EventDBService() {
        super(new EventDatabaseRepo(
                DatabaseCredentials.getUsername(),
                DatabaseCredentials.getPassword(),
                new EventValidator()
        ));
    }

}
