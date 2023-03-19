package com.map222.sfmc.socialnetworkfx.domain.business;


import com.map222.sfmc.socialnetworkfx.domain.utils.Pair;

public class Request extends Entity<Pair<String, String>>{
    private String status;

    public Request(String sender, String receiver, String state) {
        this.setID(new Pair<>(sender, receiver));
        this.status = state;
    }

    public Request(String sender, String receiver) {
        this.setID(new Pair<>(sender, receiver));
        this.status = "pending";
    }

    public String get_sender(){return this.getID().getFirstOfPair();}

    public String get_receiver(){return this.getID().getSecondOfPair();}

    public String get_status(){return this.status;}

    public void set_status(String state){this.status = state;}

    @Override
    public String toString() {
        String toS = "sender: " + this.getID().getFirstOfPair() + ", receiver: " + this.getID().getSecondOfPair() + ", status: " + get_status();
        return toS;
    }

}
