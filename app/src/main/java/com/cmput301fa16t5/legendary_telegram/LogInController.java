package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;

/**
 * Controller for Log In Screen Activity
 * Very simple, given the activity.
 */
public class LogInController {

    private CentralController centralCommand;

    public LogInController() {
        centralCommand = CentralController.getInstance();
    }

    /**
     * Activity class passes EditText input. Controller checks if valid through Gson Controller.
     * If so, it'll tell the singleton to save whatever it has and load the new user.
     * Else the Activity will be notified to post a toast.
     * @param userName: Username from activity class EditText
     * @param context: Needed for Gson
     * @return True if valid user name. False if it does not exist.
     */
    public boolean validateUserName(String userName, Context context) {

        if (GsonController.checkIfExists(userName, context)) {
            centralCommand.saveCurrentUser(context);
            centralCommand.loadNewUser(userName, context);

            return true;
        }

        return false;
    }
}
