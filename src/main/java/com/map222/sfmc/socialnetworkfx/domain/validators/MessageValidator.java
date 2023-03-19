package com.map222.sfmc.socialnetworkfx.domain.validators;


import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;

import java.util.regex.Pattern;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        String errors = "";
        String messageText = entity.getMessage();

        Pattern messageValidFormat = Pattern.compile("^[ A-Za-z0-9:@#%&*+\\-/?!,.<>()^_]+$");
        if(!messageValidFormat.matcher(messageText).matches())
            errors += "\n\033[5;93mMessage could not be sent (Forbidden characters encountered)\033[0m";

        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
