package com.map222.sfmc.socialnetworkfx.domain.utils;

import java.util.Arrays;
import java.util.List;

public enum DateConstants {;
    private static List<String> months = Arrays.asList(
            "JANUARY",
            "FEBRUARY",
            "MARCH",
            "APRIL",
            "MAY",
            "JUNE",
            "JULY",
            "AUGUST",
            "SEPTEMBER",
            "OCTOBER",
            "NOVEMBER",
            "DECEMBER"
    );

    /**
     * @return a list of calendar months as uppercase strings (e.g. "JANUARY")
     */
    public static List<String> Months() {
        return months;
    }
}
