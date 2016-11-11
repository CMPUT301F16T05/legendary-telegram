package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.util.Log;

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

    //set user as rider
    public void setUserRider(){
        currentUser.setAsRider();
    }

    //check if user name is valid
    public boolean CheckUserName(String username, Context context){
        if(GsonController.checkIfExists(username, context)){
            return true;
        }else{
            return false;
        }
    }

    //save changes to user profile
    public void CreateUser(User newUser, Context context){
        GsonController.saveUserToDisk(newUser, context);
    }
    //delete user from gson
    public void DeleteUser(String oldUser, Context context){
        GsonController.deleteOldUserName(oldUser, context);
    }

    //set user as driver
    public void setUserDriver(){
        currentUser.setAsDriver();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Wrapper for the add task of the ElasticSearchController
     * Any additional logic involved should be handled outside of this function
     * @param requests a vararg of requests to add to the server
     */
    public void addNewRequest(Request... requests){
        for (Request r : requests){
            //Should only add if request is not on server, no ID in other words
            if (r.getId() == null){
                //Start the task and place it on the server
                ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
                try{
                    addRequestsTask.execute(r);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Log.i("ESCErr","Tried to add something already on the server. Delete first or call update instead");
            }
        }
    }

    /**
     * Wrapper for the update task of the ElasticSearchController
     * Any additional logic involved should be handled outside of this function
     * @param requests a vararg of requests to update on the server
     */
    public void updateRequest(Request... requests){
        for (Request r : requests){
            //Should only update if request is on server, has an ID in other words
            if (r.getId() != null){
                //Start the task and place it on the server
                ElasticSearchController.UpdateRequestsTask updateRequestsTask = new ElasticSearchController.UpdateRequestsTask();
                try{
                    updateRequestsTask.execute(r);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Log.i("ESCErr","Tried to update something that wasn't on the server");
            }
        }
    }
}
