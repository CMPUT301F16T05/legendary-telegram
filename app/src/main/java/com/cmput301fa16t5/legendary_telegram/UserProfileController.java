package com.cmput301fa16t5.legendary_telegram;

/**
 * User Profile Controller, checks if entered information is any good.
 *
 * Does all the parsing of information from User Activity to see if it's correct.
 * If it is, uses CentralController to proceed further with data edits.
 * @author kgmills
 */
public class UserProfileController {

    private final CentralController centralCommand;
    private final User currentUser;

    /**
     * Constructor.
     * Gets the current user.
     */
    public UserProfileController() {
        centralCommand = CentralController.getInstance();
        currentUser = centralCommand.getCurrentUser();
    }

    /**
     * Checks to see if the String corresponds to an existing username (or is invalid, like "");
     * @param username The String to be checked.
     * @return True if username is invalid. False otherwise.
     */
    public boolean invalidateName(String username){
        return username.matches("") || centralCommand.checkUserName(username);
    }

    /**
     * Gets the information needed to fill the views in the event that we are editing a user profile.
     * @return A String[] containing the username, email, telephone and vehicle fields.
     */
    public String[] getProfileData() {
        String[] data = new String[4];

        data[0] = currentUser.getUserName();
        data[1] = currentUser.getEmail();
        data[2] = currentUser.getTelephone();
        data[3] = currentUser.getVehicle();

        return data;
    }


    /**
     * this method will be used when the UserProfile Activity is called from MainRequestActivity
     * @param name String type username from Edit Text
     * @param email String type email from Edit Text
     * @param phone String type phone from Edit Text
     * @param vehicle String type  from Edit Text
     * @return Enum describing if the input was good or bad.
     */
    public UserProfileEnum attemptEditUser(String name, String email, String phone, String vehicle) {
        if (!name.equals(currentUser.getUserName()) && centralCommand.checkUserName(name) && name.equals("")) {
            return UserProfileEnum.badNameMemory;
        }

        UserProfileEnum theEnum= checkUserInput(name, email, phone);

        if (!theEnum.equals(UserProfileEnum.allValid)) {
            return theEnum;
        }
        String oldName = currentUser.getUserName();

        currentUser.setUserName(name);
        currentUser.setEmail(email);
        currentUser.setTelephone(phone);
        currentUser.setVehicle(vehicle);

        centralCommand.deleteUser(oldName);
        centralCommand.saveCurrentUser();

        return theEnum;
    }

    /**
     * Attempts to create a new user.
     * @param name The new user's name.
     * @param email Their email
     * @param phone Their phone number
     * @param vehicle Their vehicle description (optional)
     * @return Enum describing if the attempt was good or bad.
     */
    public UserProfileEnum attemptNewUser(String name, String email, String phone, String vehicle) {
        if (centralCommand.checkUserName(name)) {
            return UserProfileEnum.badNameMemory;
        }

        UserProfileEnum theEnum= checkUserInput(name, email, phone);

        if (!theEnum.equals(UserProfileEnum.allValid)) {
            return theEnum;
        }

        centralCommand.createNewUser(name, email, phone, vehicle);
        return theEnum;
    }

    /**
     * This function checks the Username, Email, and Phone fields for proper syntax.
     * It does not check the Username to see if it's already taken, that has been handled before
     * this function would be called.
     * We do not check the vehicle field for brand, year, etc. The user can put WHATEVER they
     * want there, the queation is, will what they put appeal to potential Riders?
     * @param name The string entered to the Username
     * @param email The string entered to the Email
     * @param phone The string entered to the phone field.
     * @return An Enum describing the validity or problem with the inputs.
     */
    private UserProfileEnum checkUserInput(String name, String email, String phone) {

        try{
            Double.parseDouble(phone.replaceAll("-", "").replaceAll(" ", ""));
        }

        catch (NumberFormatException e) {
            return UserProfileEnum.badPhone;
        }

        if ((!email.contains("@") && (!email.contains(".")))) {
            return UserProfileEnum.badEmail;
        }

        // There are probably others...
        String[] badFileCharacters = {"|", "\"", "/", ".", "?", ">", "<", "+", "=", ":", "*"};
        for (String s: badFileCharacters) {
            if (name.contains(s)) {
                return UserProfileEnum.badNameStyle;
            }
        }

        return UserProfileEnum.allValid;
    }
}
