package com.map222.sfmc.socialnetworkfx.domain.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<String> {
    private String firstName;
    private String lastName;
    private List<String> friends;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) { this.friends = friends; }

    public void addFriend(String friend) {
        friends.add(friend);
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<>();
        this.amendDelete();
    }

    @Override
    public String toString() {
        String toS = "username: " + getID() + " | First Name: " + getFirstName() + ", Last Name: " + getLastName() + "\n          Friends: ";
        if(friends == null || friends.size() == 0)
            toS += "none";
        else for(String u: friends)
            toS += u + " ";
        return toS;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(!(o instanceof User))
            return false;
        User otherUser = (User) o;

        return getFirstName().equals(otherUser.getFirstName()) &&
                getLastName().equals(otherUser.getLastName()) &&
                getFriends().equals(otherUser.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID(), firstName, lastName, friends);
    }
}
