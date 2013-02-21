package com.rickdane.farmersmarkets.views;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/20/13
 * Time: 9:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class YesNoFieldDisplayHelper {

    private String displayYesValue;
    private String displayNoValue;

    public YesNoFieldDisplayHelper(String displayYesValue, String displayNoValue) {
        this.displayYesValue = displayYesValue;
        this.displayNoValue = displayNoValue;
    }


    /**
     * Converts value from db into Appropriate Display for yes / no
     */

    public String convertToYesNo(String inputValue) {
        if (inputValue.toLowerCase().trim().equals("y")) {
            return displayYesValue;
        }

        return displayNoValue;
    }

}
