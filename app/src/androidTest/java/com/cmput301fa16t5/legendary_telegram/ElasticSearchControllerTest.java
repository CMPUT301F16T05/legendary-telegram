package com.cmput301fa16t5.legendary_telegram;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by Randy on 2016-11-06.
 * Just a note for these tests, many of them call
 * get() and sleep(). These are to ensure the AsyncTask
 * thread has finished. These will slow the tests
 * down noticably but is a nescessary evil for
 * as synchronization is needed for proper testing.
 * These should NOT be called outside of the test...
 * well except for getRequests, that needs get() obviously
 */
public class ElasticSearchControllerTest {

    private static final int sleepTimer = 2;

    /**
     * Remove any test requests sent to the server.
     * This is not functionally important but allows
     * for clean tests with no footprint
     * NOTE: by nescessity this is run at the end of
     * the test. If it fails then this will not
     * be called and server will need to cleaned of
     * test manually.
     */
    public void cleanUpRequests(Request... requests){
        ElasticSearchController.DeleteRequestsTask deleteRequestsTask = new ElasticSearchController.DeleteRequestsTask();
        deleteRequestsTask.execute(requests);
    }

    @Test
    public void testAddRequest(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, new LatLng(0, 0), new LatLng(1, 1));

        //Make sure that we're dealing with a newly made, offline request
        assertFalse(newRequest.isOnServer());
        assertTrue(newRequest.getId() == null);

        //Start the task and place it on the server
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            addRequestsTask.execute(newRequest).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertTrue(newRequest.isOnServer());
        Log.i("DEBUG", "Added " + newRequest.getId());
        assertFalse(newRequest.getId() == null);
        cleanUpRequests();
    }

    @Test
    public void testUpdateRequest(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, new LatLng(0, 0), new LatLng(1, 1));
        ArrayList<Request> gotRequest = new ArrayList();

        //Make sure that we're dealing with a newly made, offline request
        assertFalse(newRequest.isOnServer());
        assertTrue(newRequest.getId() == null);

        //Start the task and place it on the server
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            addRequestsTask.execute(newRequest).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertTrue(newRequest.isOnServer());
        Log.i("DEBUG", "Added " + newRequest.getId());
        assertFalse(newRequest.getId() == null);

        //Change a value and call an update so the change is reflected on the server
        newRequest.commitToRequest();
        ElasticSearchController.UpdateRequestsTask updateRequestsTask = new ElasticSearchController.UpdateRequestsTask();
        try{
            updateRequestsTask.execute(newRequest).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(ElasticSearchQueries.ID, newRequest.getId()).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(gotRequest.isEmpty());
        assertEquals(RequestEnum.driverHasCommitted, gotRequest.get(0).getState());

        cleanUpRequests();
    }

    @Test
    public void testGetRequestFromID(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, new LatLng(0, 0), new LatLng(1, 1));
        ArrayList<Request> gotRequest = new ArrayList();

        //Make sure that we're dealing with a newly made, offline request
        assertFalse(newRequest.isOnServer());
        assertTrue(newRequest.getId() == null);
        assertTrue(gotRequest.isEmpty());

        //Place new request on server and wait for synchronisation
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            addRequestsTask.execute(newRequest).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Retrieve the newly added request
        assertTrue(newRequest.isOnServer());
        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(ElasticSearchQueries.ID, newRequest.getId()).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(gotRequest.isEmpty());
        assertEquals(newRequest.getId(), gotRequest.get(0).getId());
        cleanUpRequests(newRequest);
    }

    //Extra bonus of testing multiple returns of each test
    @Test
    public void testGetRequestByGeoDistance(){
        Request far = new Request(null, new LatLng(0, 0), new LatLng(1, 1));
        Request near1 = new Request(null, new LatLng(90, 90), new LatLng(89.9, 89.9));
        Request near2 = new Request(null, new LatLng(89.9, 89.9), new LatLng(90, 90));
        ArrayList<Request> gotRequest = new ArrayList<>();

        //Ensure everything is initialized correctly
        assertFalse(far.isOnServer());
        assertTrue(far.getId() == null);
        assertFalse(near1.isOnServer());
        assertTrue(near1.getId() == null);
        assertFalse(near2.isOnServer());
        assertTrue(near2.getId() == null);
        assertTrue(gotRequest.isEmpty());

        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            addRequestsTask.execute(far, near1, near2).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertTrue(far.isOnServer());
        assertFalse(far.getId() == null);
        assertTrue(near1.isOnServer());
        assertFalse(near1.getId() == null);
        assertTrue(near2.isOnServer());
        assertFalse(near2.getId() == null);

        //TODO FIGURE OUT LATLNG TO STRING
        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(ElasticSearchQueries.GEODISTANCE, "THINGS").get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteRequest(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, new LatLng(0, 0), new LatLng(1, 1));
        ArrayList<Request> gotRequest = new ArrayList<>();

        //Make sure that we're dealing with a newly made, offline request
        assertFalse(newRequest.isOnServer());
        assertTrue(newRequest.getId() == null);
        assertTrue(gotRequest.isEmpty());

        //Add the request we will be removing
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try {
            addRequestsTask.execute(newRequest).get();
        }catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(newRequest.isOnServer());

        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Ensure we are deleting the correct request
        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(ElasticSearchQueries.ID, newRequest.getId()).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(gotRequest.isEmpty());
        assertEquals(newRequest.getId(), gotRequest.get(0).getId());

        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Delete the request
        ElasticSearchController.DeleteRequestsTask deleteRequestsTask = new ElasticSearchController.DeleteRequestsTask();
        deleteRequestsTask.execute(newRequest);
        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(newRequest.getId() == null);

        //Now try and retrieve the newly deleted request. No result is expected.
        ElasticSearchController.GetRequests getRequestsTask2 = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask2.execute(ElasticSearchQueries.ID, newRequest.getId()).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(gotRequest.isEmpty());
    }
}
