package com.map222.sfmc.socialnetworkfx.domain.business;

import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Message extends Entity<UUID> {
    private User from;
    private User to;
    private String message;
    private LocalDateTime date;
    private UUID reply;

    public Message(User sender, User receiver, String messageContent) {
        this.setID(UUID.randomUUID());
        this.from = sender;
        this.to = receiver;
        this.message = messageContent;
        this.date = LocalDateTime.now();
        this.reply = null;
    }

    public Message(User sender, User receiver, String messageContent, LocalDateTime date) {
        this.setID(UUID.randomUUID());
        this.from = sender;
        this.to = receiver;
        this.message = messageContent;
        this.date = date;
        this.reply = null;
    }

    public User getSender() {
        return from;
    }

    public void setSender(User sender) {
        this.from = sender;
    }

    public User getReceiver() {
        return to;
    }

    public void setReceiver(User receiver) {
        this.to = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UUID getReplyRoot() {
        return reply;
    }

    public void setReplyRoot(UUID reply) {
        this.reply = reply;
    }

    public User getFriend(User user) {
        if(Objects.equals(this.to.getID(), user.getID()))
            return this.from;
        if(Objects.equals(this.from.getID(), user.getID()))
            return this.to;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return from.equals(message1.from) && to.equals(message1.to) && message.equals(message1.message) && date.equals(message1.date) && reply.equals(message1.reply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message, date, reply);
    }
}
