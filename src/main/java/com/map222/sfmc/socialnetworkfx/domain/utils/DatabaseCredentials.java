package com.map222.sfmc.socialnetworkfx.domain.utils;

public enum DatabaseCredentials {;
    /**
     * URL for the PostgreSQL database
     */
    private static final String url = "jdbc:postgresql://localhost:5432/socialNetwork";

    /**
     * Username for the PostgreSQL database
     */
    private static final String username = "postgres";

    /**
     * Password for the PostgreSQL database
     */
    private static final String password = "43813328";

    /**
     * URL for the PostgreSQL database
     */
    public static String getUrl() { return url; }

    /**
     * Username for the PostgreSQL database
     */
    public static String getUsername() { return username; }

    /**
     * Password for the PostgreSQL database
     */
    public static String getPassword() { return password; }
}
