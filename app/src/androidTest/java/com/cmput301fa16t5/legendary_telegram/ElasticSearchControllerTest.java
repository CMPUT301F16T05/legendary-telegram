package com.cmput301fa16t5.legendary_telegram;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by Randy on 2016-11-06.
 *
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
        Request newRequest = new Request(null, null, null);

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
        assertFalse(newRequest.getId().isEmpty());
        cleanUpRequests();
    }

    @Test
    public void testGetRequestFromID(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, null, null);
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
            gotRequest = getRequestsTask.execute("id", newRequest.getId()).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(gotRequest.isEmpty());
        assertEquals(newRequest.getId(), gotRequest.get(0).getId());
        cleanUpRequests(newRequest);
    }

    @Test
    public void testDeleteRequest(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, null, null);
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
            gotRequest = getRequestsTask.execute("id", newRequest.getId()).get();
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
            gotRequest = getRequestsTask2.execute("id", newRequest.getId()).get();
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
