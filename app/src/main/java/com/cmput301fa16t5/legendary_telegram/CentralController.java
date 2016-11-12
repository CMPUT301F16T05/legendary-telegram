package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

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

    //set user as driver
    public void setUserDriver(){
        currentUser.setAsDriver();
    }

    //check if user name is valid
    public boolean checkUserName(String username, Context context){
        return GsonController.checkIfExists(username, context);
    }

    //delete user from gson
    public void deleteUser(String oldUser, Context context){
        GsonController.deleteOldUserName(oldUser, context);
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

    /**
     * Wrapper for ElasticSearch get that focuses on ID
     * @param searchItems an array of IDs to look for
     * @return an ArrayList of requests that match the query
     */
    public ArrayList<Request> getRequestsByID(String... searchItems){
        ArrayList<Request> gotRequest = new ArrayList<>();
        String[] newSearchItems = new String[searchItems.length+1];

        //Parse a new array to add as a new vararg
        newSearchItems[0] = ElasticSearchQueries.ID;
        for (int q = 0;q<searchItems.length;q++){
            newSearchItems[q+1] = searchItems[q];
        }

        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(newSearchItems).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return gotRequest;
    }

    public ArrayList<Request> getRequestsByGeoDistance(LatLng near){
        ArrayList<Request> gotRequest = new ArrayList<>();
        String parsedString = String.valueOf(near.latitude) + "," + String.valueOf(near.longitude);
        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(ElasticSearchQueries.GEODISTANCE, parsedString).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return gotRequest;
    }

    /**
     * Wrapper for ElasticSearch get that grabs the first few requests
     * @return an ArrayList of requests that match the query
     */
    public ArrayList<Request> getRequestsAll() {
        ArrayList<Request> gotRequest = new ArrayList<>();

        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(ElasticSearchQueries.ALL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gotRequest;
    }

    public void createNewUser(String name, String email, String phone, String vehicle, Context context) {
        User newbie = new User(name, email, phone);

        if ((vehicle != null) || (!vehicle.equals(""))) {
            newbie.setVehicle(vehicle);
        }

        GsonController.saveUserToDisk(newbie, context);
    }

    public ArrayList<Request> getRequests() {
        ArrayList<Request> myList = currentUser.getMyCurrentMode().openRequests;

        if (myList == null) {
            return new ArrayList<>();
        }
        return myList;
    }

    public ArrayList<IdentificationCard> getCards() {
        return currentUser.getMyDriver().getCurrentRequest().getPotentialDrivers();
    }

    public boolean selectCurrentRequest(int index) {
        if (this.currentUser.askForMode()) {
            this.currentUser.generateDriverCR(index);
            return true;
        }

        else {
            this.currentUser.getMyRider().setCurrentRequest(index);
            return false;
        }
    }

    public boolean canBeDriver(Context context) {
        if ((currentUser.getVehicle() == null) || (currentUser.getVehicle().equals(""))) {
            return false;
        }
        currentUser.setAsDriver();
        saveCurrentUser(context);
        return true;
    }

    public void createRequest(ArrayList<LatLng> positionPair) {
        // Rider creates srequest
        IdentificationCard me = new IdentificationCard(currentUser.getUserName(),
                currentUser.getTelephone(), currentUser.getEmail());
        Request rToUpload = currentUser.getMyRider().createNewRequest(me,
                positionPair.get(0), positionPair.get(1));
        addNewRequest(rToUpload);
    }

    public void searchRequests(ArrayList<LatLng> positionPair) {
        // Driver searches for requests
        // position is positionPair.get(0)
        ArrayList<Request> driverRequests =
                getRequestsByGeoDistance(positionPair.get(0));
        currentUser.getMyDriver().setOpenRequests(driverRequests);

    }


}
