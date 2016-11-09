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

    private static final int sleepTimer = 5;

    /**
     * Remove any test requests sent to the server.
     * This is not functionally important but allows
     * for clean tests with no footprint
     */
    public void cleanUpRequests(Request... requests){
        ArrayList<String> ids = new ArrayList<>();
        //Collect valid ids from the requests
        for (Request r: requests){
            if (!r.getId().isEmpty()) {
                ids.add(r.getId());
            }
        }

        ElasticSearchController.DeleteRequestsTask deleteRequestsTask = new ElasticSearchController.DeleteRequestsTask();
        deleteRequestsTask.execute(ids.toArray(new String[ids.size()]));
    }

    @Test
    public void testAddRequest(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, null, null);
        assertFalse(newRequest.isOnServer());

        //Initialize the ESC task and override onPostExecute so I can synchronize the test
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            addRequestsTask.execute(newRequest).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //The server wil grant an ID when added sucessfully
        assertTrue(newRequest.isOnServer());

        cleanUpRequests();
    }

    @Test
    public void testGetRequestFromID(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, null, null);
        ArrayList<Request> gotRequest = new ArrayList();
        assertFalse(newRequest.isOnServer());
        assertTrue(gotRequest.isEmpty());

        //Initialize the ESC task and override onPostExecute so I can synchronize the test
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
        //When initialized a request will have an empty ID
        assertFalse(newRequest.isOnServer());

        //Initialize the ESC task and override onPostExecute so I can synchronize the test
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

        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute("id", newRequest.getId()).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(gotRequest.isEmpty());

        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ElasticSearchController.DeleteRequestsTask deleteRequestsTask = new ElasticSearchController.DeleteRequestsTask();
        deleteRequestsTask.execute(newRequest.getId());

        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Now try a get
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
