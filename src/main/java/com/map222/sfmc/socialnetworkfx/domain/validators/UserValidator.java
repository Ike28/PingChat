package com.map222.sfmc.socialnetworkfx.domain.validators;


import com.map222.sfmc.socialnetworkfx.domain.business.User;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;

import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";

        Pattern alphabetical = Pattern.compile("^[ A-Za-z'\\-]+$");
        if(!alphabetical.matcher(entity.getFirstName()).matches())
            errors += "\nFirst name must only contain letters and, if needed, spaces.";
        if(!alphabetical.matcher(entity.getLastName()).matches())
            errors += "\nLast name must only contain letters and, if needed, spaces.";
        if(entity.getFirstName().equals(""))
            errors += "\nFirst name cannot be empty or contain only spaces.";
        if(entity.getLastName().equals(""))
            errors += "\nLast name cannot be empty or contain only spaces.";

        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
