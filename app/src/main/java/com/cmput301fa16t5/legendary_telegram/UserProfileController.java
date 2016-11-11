package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by keith on 11/2/2016.
 */

public class UserProfileController {

    private CentralController centralCommand;

    public UserProfileController() {
        centralCommand = CentralController.getInstance();
    }

    public User GetCurrentUser(){
        return centralCommand.getCurrentUser();
    }


    public boolean ValidateName(String username, Context context){
        if(centralCommand.CheckUserName(username,context)){
            return true;
        }else{
            return false;
        }
    }

    public void SaveChanges(User newUser, Context context){
        centralCommand.CreateUser(newUser, context);
    }
    public void DeleteOldUsers(String oldUser, Context context){
        centralCommand.DeleteUser(oldUser, context);
    }

    public boolean CompareName(User user, EditText editTextName, EditText phone, EditText email, EditText vehicle){
        if(centralCommand.getCurrentUser().getUserName().equals(editTextName)){
            centralCommand.getCurrentUser().setTelephone(phone.getText().toString());
            centralCommand.getCurrentUser().setEmail(email.getText().toString());
            centralCommand.getCurrentUser().setVehicle(vehicle.getText().toString());
            return true;
        }else{
            return false;
        }
    }
}
