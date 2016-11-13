package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by keith on 11/2/2016.
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
     * @param context Needed for GSON
     * @return True if we're good. If false, the username is invalid.
     */
    public boolean validateName(String username, Context context){
        if(centralCommand.checkUserName(username, context)){
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
     * @param context just needed here
     * @return false if username is changed, already exist or empty, else delete the old user gson file
     * and create a new one, and return true
     */
    public boolean attemptEditUser(String name, String email, String phone, String vehicle, Context context) {
        if (!name.equals(currentUser.getUserName()) && centralCommand.checkUserName(name, context) && name.equals("")) {
            return false;
        }

        String oldName = currentUser.getUserName();

        currentUser.setUserName(name);
        currentUser.setEmail(email);
        currentUser.setTelephone(phone);
        currentUser.setVehicle(vehicle);

        centralCommand.deleteUser(oldName, context);
        centralCommand.saveCurrentUser(context);

        return true;
    }

    /**
     * Attempts to create a new user.
     * @param name The new user's name.
     * @param email Their email
     * @param phone Their phone number
     * @param vehicle Their vehicle description (optional)
     * @param context Needed for GSON.
     * @return True if the new user could be created. False if it couldn't.
     */
    public boolean attemptNewUser(String name, String email, String phone, String vehicle, Context context) {
        if (centralCommand.checkUserName(name, context)) {
            return false;
        }

        centralCommand.createNewUser(name, email, phone, vehicle, context);
        return true;
    }
}
