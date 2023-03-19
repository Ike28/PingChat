package com.map222.sfmc.socialnetworkfx.domain.business;

import java.io.Serializable;

public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 1110L;
    private ID id;
    private boolean deleted;

    public ID getID() {
        return id;
    }
    public void setID(ID id) {
        this.id = id;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void delete() {
        deleted = true;
    }
    public void amendDelete() {
        deleted = false;
    }
}
