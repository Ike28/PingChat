package com.map222.sfmc.socialnetworkfx.service.database;

import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.utils.DatabaseCredentials;
import com.map222.sfmc.socialnetworkfx.domain.validators.MessageValidator;
import com.map222.sfmc.socialnetworkfx.repository.database.MessageDatabaseRepo;
import com.map222.sfmc.socialnetworkfx.service.Service;

import java.util.UUID;

public class MessageDBService extends Service<UUID, Message> {
    private MessageDatabaseRepo repo;

    public MessageDBService(MessageDatabaseRepo repo) {
        super(repo);
        this.repo = repo;
    }

    public MessageDBService(UserDBService userService) {
        super(new MessageDatabaseRepo(DatabaseCredentials.getUrl(),
                DatabaseCredentials.getUsername(),
                DatabaseCredentials.getPassword(),
                new MessageValidator(),
                userService.getRepo()));
        this.repo = new MessageDatabaseRepo(DatabaseCredentials.getUrl(),
                DatabaseCredentials.getUsername(),
                DatabaseCredentials.getPassword(),
                new MessageValidator(),
                userService.getRepo());
    }

    public Iterable<Message> getAll(User user1, User user2) {
        return this.repo.getAll(user1, user2);
    }

}
