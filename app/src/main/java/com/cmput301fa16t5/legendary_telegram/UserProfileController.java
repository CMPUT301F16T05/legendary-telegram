package com.cmput301fa16t5.legendary_telegram;

/**
 * User Profile Controller.
 * Does all the parsing of information from User Activity to see if it's correct.
 * If it is, uses CentralController to proceed further with data edits.
 * @author keith
 */
public class UserProfileController {

    private CentralController centralCommand;
    private User currentUser;

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
        if (username.matches("")) {
            return true;
        }

        if(centralCommand.checkUserName(username)){
            return true;
        }else{
            return false;
        }
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
     * @return false if username is changed, already exist or empty, else delete the old user gson file
     * and create a new one, and return true
     */
    public boolean attemptEditUser(String name, String email, String phone, String vehicle) {
        if (!name.equals(currentUser.getUserName()) && centralCommand.checkUserName(name) && name.equals("")) {
            return false;
        }

        String oldName = currentUser.getUserName();

        currentUser.setUserName(name);
        currentUser.setEmail(email);
        currentUser.setTelephone(phone);
        currentUser.setVehicle(vehicle);

        centralCommand.deleteUser(oldName);
        centralCommand.saveCurrentUser();

        return true;
    }

    /**
     * Attempts to create a new user.
     * @param name The new user's name.
     * @param email Their email
     * @param phone Their phone number
     * @param vehicle Their vehicle description (optional)
     * @return True if the new user could be created. False if it couldn't.
     */
    public boolean attemptNewUser(String name, String email, String phone, String vehicle) {
        if (centralCommand.checkUserName(name)) {
            return false;
        }

        centralCommand.createNewUser(name, email, phone, vehicle);
        return true;
    }
}
