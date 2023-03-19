package com.map222.sfmc.socialnetworkfx.domain.utils;

/**
 * Used in cmd shell UI classes
 */
public enum ManifestOption {
    /**
     * User manifested option for a CRUD operation
     */
    CRUDOP,
    /**
     * User manifested option for a statistics operation
     */
    STATOP,
    /**
     * User manifested option for launching a UI submenu
     */
    SUBMENU,
    /**
     * User manifested option for exiting a UI instance
     */
    EXIT,
    /**
     * Default for user manifested option of an unidentified/invalid operation
     */
    UNHANDLED
}
