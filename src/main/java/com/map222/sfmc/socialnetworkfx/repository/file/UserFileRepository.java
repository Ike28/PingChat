package com.map222.sfmc.socialnetworkfx.repository.file;


import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.validators.Validator;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.FileDataException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserFileRepository extends AbstractFileRepository<String, User> {
    public UserFileRepository(String filename, Validator<User> validator) {
        super(filename, validator);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2));
        user.setID(attributes.get(0));
        if(attributes.get(3).strip().equals("false"))
            user.amendDelete();
        else user.delete();
        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getID() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.isDeleted();
    }

    /**
     * Reads users data and their friendship data from default data files
     */
    @Override
    protected void loadData() {
        super.loadData();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/friends.csv"));
            BufferedWriter logWriter = new BufferedWriter(new FileWriter("data/invalidFriendsLog.csv", true));
            BufferedWriter deletedWriter = new BufferedWriter(new FileWriter("data/deletedFriendsLog.csv", true));

            boolean friendsDataConflicts = false;
            String line;
            List<String> validLines = new ArrayList<>();
            List<String> attributes;
            while ((line = reader.readLine()) != null && !line.equals("")) {
                attributes = Arrays.asList(line.split(";"));
                if(attributes.size() < 2) {
                    logWriter.append(line).append("\n");
                    friendsDataConflicts = true;
                    continue;
                }

                User user1 = this.entities.get(attributes.get(0));
                User user2 = this.entities.get(attributes.get(1));

                if(user1 != null && user2 !=null) {
                    if(!user1.isDeleted() && !user2.isDeleted()) {
                        user1.addFriend(user2.getID());
                        user2.addFriend(user1.getID());
                        this.entities.put(user1.getID(), user1);
                        this.entities.put(user2.getID(), user2);
                        validLines.add(line);
                    }
                    else {
                        deletedWriter.append(line).append("\n");
                    }
                }
                else {
                    logWriter.append(line).append("\n");
                    friendsDataConflicts = true;
                }
            }
            logWriter.close();
            deletedWriter.close();

            BufferedWriter returner = new BufferedWriter(new FileWriter("data/friends.csv"));

            for(String eachLine: validLines) {
                returner.append(eachLine).append("\n");
            }
            returner.close();

            if(friendsDataConflicts)
                throw new FileDataException("\n!! One or more friendships could not be created and were logged in data/invalidFriendsLog.csv");
        }
        catch(IOException ioe) {
            System.out.println("\nERROR IN READING DATA FILES\n");
            ioe.printStackTrace();
        }
        catch(FileDataException fde) {
            System.out.println(fde.getMessage());
        }
    }
}
