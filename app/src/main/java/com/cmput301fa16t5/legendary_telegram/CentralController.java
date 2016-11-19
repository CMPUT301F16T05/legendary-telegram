package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

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
    private ContextFactory myCFact;
    private ArrayObserver myObs = null;

    /**
     * Initializes the ContextFactory and gives it the object.
     * @param context Context object (typically) from LogInActivity
     */
    public void receiveAndSetContext(Context context) {
        this.myCFact = new ContextFactory(context);
    }
    /**
     * Save current user instance to disk.
     */
    public void saveCurrentUser() {
        if (getCurrentUser() != null) {
            GsonController.saveUserToDisk(getCurrentUser(), myCFact.getGsonContext());
        }
    }

    /**
     * Load a new User instance from disk.
     * @param userName  Name of the new User instance to be loaded.
     */
    public void loadNewUser(String userName) {
        setCurrentUser(GsonController.loadUserInfo(userName, myCFact.getGsonContext()));
    }

    /**
     * Getter for the current User.
     * @return Current instance of user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user to use it's Rider instance.
     */
    public void setUserRider(){
        currentUser.setAsRider();
        saveCurrentUser();
    }

    /**
     * Checks if the current user can be a Driver (has filled out vehicle field)
     * and if so, sets them as it.
     * @return True if they can be a Driver. False otherwise.
     */
    public boolean canBeDriver() {
        if ((currentUser.getVehicle() == null) || (currentUser.getVehicle().equals(""))) {
            return false;
        }
        currentUser.setAsDriver();
        saveCurrentUser();
        return true;
    }

    /**
     * Uses the GSON controller to check if a given username is valid.
     * I.E. Does that user exist on disk already.
     * @param username Name of user to be checked.
     * @return True if user exists. False otherwise.
     */
    public boolean checkUserName(String username){
        return GsonController.checkIfExists(username, myCFact.getGsonContext());
    }

    /**
     * Orders Gson to delete a User from disk.
     * @param oldUser User to be deleted.
     */
    public void deleteUser(String oldUser){
        GsonController.deleteOldUserName(oldUser, myCFact.getGsonContext());
    }

    /**
     * Setter for current user.
     * @param currentUser
     */
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

    /**
     * Wrapper for ElasticSearch get that focuses on the request fee
     * @param min the lower bound of the fee to look between
     * @param max the upper bound of the fee to look between
     * @param usePerKM boolean that indicates which field, fee or feeperkm, to look at
     * @return an ArrayList of requests that match the query
     */
    public ArrayList<Request> getRequestsByFee(double min, double max, Boolean usePerKM) {
        ArrayList<Request> gotRequest = new ArrayList<>();
        String parsedString[] = new String[2];
        if (usePerKM) {
            parsedString[0] = ElasticSearchQueries.FEEPERKM;
        } else {
            parsedString[0] = ElasticSearchQueries.FEE;
        }
        parsedString[1] = String.valueOf(min);
        parsedString[2] = String.valueOf(max);
        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(parsedString).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return gotRequest;
    }

    /**
     * Wrapper for ElasticSearch that gets the Driver's Requests.
     * @param near Latlng coordinates that requests should be near.
     * @return An ArrayList of Requests.
     */
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

    /**
     * Creates a new user and saves it to disk.
     * @param name User name of new User.
     * @param email Email address.
     * @param phone Phone number.
     * @param vehicle Possible vehicle description.
     */
    public void createNewUser(String name, String email, String phone, String vehicle) {
        User newbie = new User(name, email, phone);

        if ((vehicle != null) || (!vehicle.equals(""))) {
            newbie.setVehicle(vehicle);
        }

        GsonController.saveUserToDisk(newbie, myCFact.getGsonContext());
    }

    /**
     * Gets Requests from User, depending on whether it is acting as Rider or Driver.
     * @return An ArrayList of Requests.
     */
    public ArrayList<Request> getRequests() {
        return this.currentUser.getMyCurrentMode().getOpenRequests();
    }

    /**
     * Gets the Identification Cards corresponding to the potential Drivers of a given Request.
     * @return An ArrayList of Identificationcards
     */
    public ArrayList<IdentificationCard> getCards() {
        return currentUser.getMyRider().getCurrentRequest().getPotentialDrivers();
    }

    /**
     * Called by MainRequest Activity/Controller when a User clicks on a Request
     * Sets that to be the current Request for Rider/Driver.
     * @param index Index of the request.
     * @return True if Driver, false if Rider.
     */
    public boolean selectCurrentRequest(int index) {
        if (this.currentUser.askForMode()) {
            this.currentUser.getMyDriver().setCurrentRequest(index);
            return true;
        }

        else {
            this.currentUser.getMyRider().setCurrentRequest(index);
            return false;
        }
    }


    /**
     * Creates a new Request based on two LatLng coordinate sets.
     * @param positionPair ArrayList containing two coordinates.
     */
    public void createRequest(ArrayList<LatLng> positionPair) {
        // Rider creates srequest
        IdentificationCard me = new IdentificationCard(currentUser.getUserName(),
                currentUser.getTelephone(), currentUser.getEmail());
        Request rToUpload = currentUser.getMyRider().createNewRequest(me,
                positionPair.get(0), positionPair.get(1));
        saveCurrentUser();
        bugForUpdate();
        addNewRequest(rToUpload);
        saveCurrentUser();
    }

    /**
     * Searches for Requests based on a LatLng coordinate pair. Assigns them to the Driver.
     * @param positionPair The LatLng coordinates.
     */
    public void searchRequests(ArrayList<LatLng> positionPair) {
        // Driver searches for requests
        // position is positionPair.get(0)
        ArrayList<Request> driverRequests =
                getRequestsByGeoDistance(positionPair.get(0));
        currentUser.getMyDriver().setOpenRequests(driverRequests);
        saveCurrentUser();

    }

    /**
     * Tells the User to generate A Driver card
     * @return An IdentificationCard using the Driver constructor.
     */
    public IdentificationCard generateDriverCard() {
        return new IdentificationCard(currentUser.getUserName(), currentUser.getTelephone(),
                currentUser.getEmail(), currentUser.getVehicle());
    }

    /**
     * Instantiate a new ArrayObserver (if not yet) and add an Adapter to it.
     * @param adapt ArrayAdapter to be added.
     */
    public void addArrayAdapter(ArrayAdapter adapt) {
        if (this.myObs == null) {
            this.myObs = new ArrayObserver(this);
        }

        this.myObs.addAdapter(adapt);
    }

    public void bugForUpdate() {
        if (this.myObs == null) {
            this.myObs = new ArrayObserver(this);
        }
        this.myObs.onButtonPress();
    }
}
