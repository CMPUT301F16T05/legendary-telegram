package com.cmput301fa16t5.legendary_telegram;

/**
 * This is just an Enum used for parsing the information in the User Profile Activity/Controller set.
 *
 * allValid,       All good to go.
 * badNameMemory,  Bad name due to already existing name/blank field
 * badNameStyle,   Bad name due to style (name is part of file used for saving data)
 * badEmail,       Invalid email (typically must contain one "@" and one "."
 * badPhone        Phone must be all numbers.
 * @author kgmills
 */
public enum UserProfileEnum {
    allValid,
    badNameMemory,
    badNameStyle,
    badEmail,
    badPhone
}
