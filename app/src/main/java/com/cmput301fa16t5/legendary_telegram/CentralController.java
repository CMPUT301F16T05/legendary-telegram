package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;

/**
 * Created by keith on 11/2/2016.
 *
 * THIS CLASS IS A SINGLETON
 * Only one exists in the system at any given time.
 * Does most of the work with GsonController and ESearchController.
 * Talks to the User class which controllers the Rider and Driver behaviour.
 * Essentially acts as a crossroad between the live data (User and it's Rider/Driver),
 * the outside world/database and the user Via the activities and their controllers.
 */
public class CentralController {
    // Singleton stuff.
    private static CentralController ourInstance = new CentralController();
    public static CentralController getInstance() {
        return ourInstance;
    }
    private CentralController() {
    }

    private User currentUser = null;

    public void saveCurrentUser(Context context) {
        if (getCurrentUser() != null) {
            GsonController.saveUserToDisk(getCurrentUser(), context);
        }
    }

    public void loadNewUser(String userName, Context context) {
        setCurrentUser(GsonController.loadUserInfo(userName, context));
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
