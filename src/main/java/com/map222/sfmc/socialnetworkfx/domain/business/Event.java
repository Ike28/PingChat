package com.map222.sfmc.socialnetworkfx.domain.business;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event extends Entity<UUID> {
    private String title;
    private LocalDateTime date;

    public Event(String title, LocalDateTime date) {
        this.setID(UUID.randomUUID());
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
