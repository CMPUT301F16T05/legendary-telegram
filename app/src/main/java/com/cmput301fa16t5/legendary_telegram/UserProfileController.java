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

    public boolean attemptEditUser(String name, String email, String phone, String vehicle, Context context) {
        if (!name.equals(currentUser.getUserName()) && centralCommand.checkUserName(name, context)) {
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
