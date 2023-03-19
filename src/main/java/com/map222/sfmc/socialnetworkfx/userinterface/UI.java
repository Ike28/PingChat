package com.map222.sfmc.socialnetworkfx.userinterface;

import com.map222.sfmc.socialnetworkfx.domain.utils.ManifestOption;

public interface UI {
    /**
     * Outputs a relevant option menu to the console
     */
    public void printMenu();

    /**
     * Manages the user's interaction with the menu
     * @param option -a string containing an option (only options from the option menu will have an effect)
     * @return a ManifestOption summarizing the user's chosen option
     */
    public ManifestOption optionHandler(String option);

    /**
     * Runs the interface with the user
     */
    public void runInterface();
}
