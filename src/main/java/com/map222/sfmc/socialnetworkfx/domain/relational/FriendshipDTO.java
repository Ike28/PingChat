package com.map222.sfmc.socialnetworkfx.domain.relational;

public class FriendshipDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String date;

    public FriendshipDTO(String firstName, String lastName, String date) {
        this.username = "";
        this.firstname = firstName;
        this.lastname = lastName;
        this.date = date;
    }

    public FriendshipDTO(String username, String firstname, String lastname, String date) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getDate() {
        return this.date;
    }

    public String toString(){
        return "Firstname : " + firstname + "\n" + "Lastname: " + lastname + "\n" + "Date: " + date + "\n";
    }
}
