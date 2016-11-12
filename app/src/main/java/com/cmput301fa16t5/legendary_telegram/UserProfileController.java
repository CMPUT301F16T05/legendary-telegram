package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by keith on 11/2/2016.
 */

public class UserProfileController {

    private CentralController centralCommand;
    private User currentUser;

    public UserProfileController() {
        centralCommand = CentralController.getInstance();
        currentUser = centralCommand.getCurrentUser();
    }

    public boolean validateName(String username, Context context){
        if(centralCommand.checkUserName(username,context)){
            return true;
        }else{
            return false;
        }
    }

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

    public boolean attemptNewUser(String name, String email, String phone, String vehicle, Context context) {
        if (centralCommand.checkUserName(name, context)) {
            return false;
        }

        centralCommand.createNewUser(name, email, phone, vehicle, context);
        return true;
    }
}
