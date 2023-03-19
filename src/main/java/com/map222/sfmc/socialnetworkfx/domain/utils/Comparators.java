package com.map222.sfmc.socialnetworkfx.domain.utils;


import com.map222.sfmc.socialnetworkfx.domain.business.Message;
import com.map222.sfmc.socialnetworkfx.domain.business.User;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public enum Comparators {;
    /**
     * Compares message hashmap entries by date
     * Used in cmd shell UI
     */
    public static Comparator<Map.Entry<UUID, Message>> dateComparator =
            (e1, e2) -> {
                Long date1Value = e1.getValue().getDate().atZone(ZoneId.systemDefault()).toEpochSecond() - Constants.earliestTimestamp;
                Long date2Value = e2.getValue().getDate().atZone(ZoneId.systemDefault()).toEpochSecond() - Constants.earliestTimestamp;
                return date1Value.compareTo(date2Value);
            };

    /**
     * Compares message objects by date
     */
    public static Comparator<Message> dateComparatorMsg =
            (e1, e2) -> {
                Long date1Value = e1.getDate().atZone(ZoneId.systemDefault()).toEpochSecond() - Constants.earliestTimestamp;
                Long date2Value = e2.getDate().atZone(ZoneId.systemDefault()).toEpochSecond() - Constants.earliestTimestamp;
                return date1Value.compareTo(date2Value);
            };

    /**
     * Compares chat history entries by the date of the most recent message
     */
    public static Comparator<Map.Entry<User, Pair<Message, List<Message>>>> dateComparatorChat =
            (e1, e2) -> {
                Long date1Value = e1.getValue().getFirstOfPair().getDate().atZone(ZoneId.systemDefault()).toEpochSecond() - Constants.earliestTimestamp;
                Long date2Value = e2.getValue().getFirstOfPair().getDate().atZone(ZoneId.systemDefault()).toEpochSecond() - Constants.earliestTimestamp;
                return date1Value.compareTo(date2Value);
            };
}
