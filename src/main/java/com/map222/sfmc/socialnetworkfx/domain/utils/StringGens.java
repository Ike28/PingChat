package com.map222.sfmc.socialnetworkfx.domain.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Provides creation of various necessary strings for parsing and cmd shell UI output
 */
public class StringGens {
    /**
     * String that marks the end of the chat history
     */
    public static final String chatFooter = "\n****************************************************************";

    /**
     * String that marks no previous messages
     */
    public static final String noMessages = "\n    No messages yet! Say Hi!\n";

    /**
     * Returns a string to mark the beginning of the chat history with another user
     * @param username -username of the other user in the chat
     * @return the final String to display
     */
    public static String chatHeader(String username) {
        return "\n****************************************************************\nChat with "
                + username
                + ":\n****************************************************************";
    }

    /**
     * Prefixes a message String with additional info about the message it replies to
     * @param message -current message
     * @param reply -the reply root message
     * @return String
     */
    public static String addReplyToMessage(String message, String reply) {
        String replyShow = reply.substring(0, Math.min(reply.length(), 10));
        if(reply.length() > 10)
            replyShow += "...";
        return "\u21B3 Replied to: " + replyShow + "$" + message;
    }

    /**
     * Creates a String timestamp to add to a String message for display
     * @param messageDate -the date of the message from the Message object
     * @return final display-friendly String timestamp
     */
    public static String getDisplayTimestamp(LocalDateTime messageDate) {
        String timestamp = messageDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        if (LocalDate.now().equals(messageDate.toLocalDate())) {
            timestamp = "Today at " + timestamp;
        }
        else if(LocalDate.now().minusDays(1).equals(messageDate.toLocalDate())) {
            timestamp = "Yesterday at " + timestamp;
        }
        else if(LocalDate.now().getYear() == messageDate.getYear()) {
            timestamp = messageDate.format(DateTimeFormatter.ofPattern("E, MMM dd")) + " at " + timestamp;
        }
        else timestamp = messageDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + " at " + timestamp;

        return timestamp;
    }

    /**
     * Aligns the message and its information to the correct side of the chat, based on sender and the connected user
     * @param messageSenderUsername -username of the sender
     * @param connectedUser -username of the connected user
     * @param message -the message as String
     * @param identifier -the identifier of the message for replying purposes
     * @param timestamp -display-friendly timestamp of the message (see StringGens.getDisplayTimestamp() )
     * @return final message as String to display
     */
    public static String messageToDisplayLayout(String messageSenderUsername, String connectedUser, String message, String identifier, String timestamp) {
        if(Objects.equals(messageSenderUsername, connectedUser))
            return "\n                               "
                    + identifier + " You | "
                    + timestamp
                    + " :\n                               "
                    + message.replace("$", "\n                               ");
        else return "\n"
                + identifier + " "
                + messageSenderUsername
                + " | "
                + timestamp
                + " :\n"
                + message.replace("$", "\n");
    }
}
