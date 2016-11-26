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
        Boolean isGood = Boolean.FALSE;

        //Make sure that we're dealing with a newly made, offline request
        assertFalse(newRequest.isOnServer());
        assertTrue(newRequest.getId() == null);

        //Start the task and place it on the server
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            isGood = addRequestsTask.execute(newRequest).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertTrue(newRequest.isOnServer());
        assertFalse(newRequest.getId() == null);
        assertTrue(isGood);
        cleanUpRequests(newRequest);
    }

    @Test
    public void testUpdateRequest(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, new LatLng(0, 0), new LatLng(1, 1));
        ArrayList<Request> gotRequest = new ArrayList();
        Boolean isGood = Boolean.FALSE;

        //Make sure that we're dealing with a newly made, offline request
        assertFalse(newRequest.isOnServer());
        assertTrue(newRequest.getId() == null);

        //Start the task and place it on the server
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            isGood = addRequestsTask.execute(newRequest).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertTrue(newRequest.isOnServer());
        assertFalse(newRequest.getId() == null);
        assertTrue(isGood);

        //Change a value and call an update so the change is reflected on the server
        newRequest.commitToRequest();
        isGood = Boolean.FALSE;
        ElasticSearchController.UpdateRequestsTask updateRequestsTask = new ElasticSearchController.UpdateRequestsTask();
        try{
            isGood = updateRequestsTask.execute(newRequest).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(isGood);

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

        cleanUpRequests(newRequest);
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

        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(ElasticSearchQueries.GEODISTANCE, "90,90").get();
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

        /* If this assertion fails the likely reason
            is that the geo_point mapping was removed
            from the server. To fix run the following
            statement in a suitable rest client
            PUT http://cmput301.softwareprocess.es:8080/fa16t5/request/_mapping
            {
                "request" : {
                    "properties" : {
                        "elasticEnd" : {"type" : "geo_point"},
                        "elasticStart" : {"type" : "geo_point"}
                    }
                }
            }
         */
        assertTrue(gotRequest.size() > 0);
        cleanUpRequests(far, near1, near2);
    }

    @Test
    public void testGetRequestByKeyword(){
        //Should not be case or location sensitive
        //Should match whole string
        //expectations:
        //Looking for "potato" will return only potato
        //Looking for "please" will return lettuce and corn
        //Looking for "fruit" will return nothing
        String potatoStr = "I want to get a potato";
        String lettuceStr = "Please lettuce go buy something";
        String cornStr = "I need to acquire corn, please. Oh and I need more potatoes as well";
        Request potato = new Request(null, new LatLng(0, 0), new LatLng(0, 0), potatoStr);
        Request lettuce = new Request(null, new LatLng(0, 0), new LatLng(0, 0), lettuceStr);
        Request corn = new Request(null, new LatLng(0, 0), new LatLng(0, 0), cornStr);

        ArrayList<Request> gotRequest = new ArrayList<>();
        Boolean isGood = Boolean.FALSE;

        //Ensure everything is initialized correctly
        assertFalse(potato.isOnServer());
        assertTrue(potato.getId() == null);
        assertFalse(lettuce.isOnServer());
        assertTrue(lettuce.getId() == null);
        assertFalse(corn.isOnServer());
        assertTrue(corn.getId() == null);
        assertTrue(gotRequest.isEmpty());

        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            isGood = addRequestsTask.execute(potato, lettuce, corn).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertTrue(isGood);
        assertTrue(potato.isOnServer());
        assertFalse(potato.getId() == null);
        assertTrue(lettuce.isOnServer());
        assertFalse(lettuce.getId() == null);
        assertTrue(corn.isOnServer());
        assertFalse(corn.getId() == null);

        //"potato" test, expectation 1
        ElasticSearchController.GetRequests getRequestsTaskPotato = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTaskPotato.execute(ElasticSearchQueries.KEYWORD, "potato").get();
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

        assertFalse(gotRequest.isEmpty());
        assertEquals(1, gotRequest.size());

        gotRequest.clear();
        assertTrue(gotRequest.isEmpty());

        //"please" test, expect 2
        ElasticSearchController.GetRequests getRequestsTaskPlease = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTaskPlease.execute(ElasticSearchQueries.KEYWORD, "please").get();
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

        assertFalse(gotRequest.isEmpty());
        assertEquals(2, gotRequest.size());

        gotRequest.clear();
        assertTrue(gotRequest.isEmpty());

        //"fruit test", expect nothing
        gotRequest.clear();
        ElasticSearchController.GetRequests getRequestsTaskFruit = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTaskFruit.execute(ElasticSearchQueries.KEYWORD, "fruit").get();
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

        gotRequest.clear();
        assertTrue(gotRequest.isEmpty());

        cleanUpRequests(potato, lettuce, corn);
    }

    //Extra bonus of testing multiple returns of each test
    @Test
    public void testGetRequestByFee(){
        Request free = new Request(null, new LatLng(0, 0), new LatLng(0, 0));
        Request cheap = new Request(null, new LatLng(0, 0), new LatLng(0.0001, 0.0001));
        Request expensive = new Request(null, new LatLng(0, 0), new LatLng(90, 90));
        ArrayList<Request> gotRequest = new ArrayList<>();

        //Ensure everything is initialized correctly
        assertFalse(free.isOnServer());
        assertTrue(free.getId() == null);
        assertFalse(cheap.isOnServer());
        assertTrue(cheap.getId() == null);
        assertFalse(expensive.isOnServer());
        assertTrue(expensive.getId() == null);
        assertTrue(gotRequest.isEmpty());

        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try{
            addRequestsTask.execute(free, cheap, expensive).get();
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
        assertTrue(free.isOnServer());
        assertFalse(free.getId() == null);
        assertTrue(cheap.isOnServer());
        assertFalse(cheap.getId() == null);
        assertTrue(expensive.isOnServer());
        assertFalse(expensive.getId() == null);

        ElasticSearchController.GetRequests getRequestsTask = new ElasticSearchController.GetRequests();
        try {
            gotRequest = getRequestsTask.execute(ElasticSearchQueries.FEE, "2.25", "2.30").get();
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

        //If this assert fails its a sign that the server may need a cleaning
        assertEquals(2, gotRequest.size());
        assertTrue(free.getId().equals(gotRequest.get(0).getId())
                || free.getId().equals(gotRequest.get(1).getId()));
        assertTrue(cheap.getId().equals(gotRequest.get(0).getId())
                || cheap.getId().equals(gotRequest.get(1).getId()));
        cleanUpRequests(free, cheap, expensive);
    }

    @Test
    public void testDeleteRequest(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, new LatLng(0, 0), new LatLng(1, 1));
        ArrayList<Request> gotRequest = new ArrayList<>();
        Boolean isGood = Boolean.FALSE;

        //Make sure that we're dealing with a newly made, offline request
        assertFalse(newRequest.isOnServer());
        assertTrue(newRequest.getId() == null);
        assertTrue(gotRequest.isEmpty());

        //Add the request we will be removing
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        try {
            isGood = addRequestsTask.execute(newRequest).get();
        }catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(newRequest.isOnServer());
        assertFalse(newRequest.getId() == null);
        assertTrue(isGood);

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

        //KISS, Keep It Simple Stupid
        try {
            TimeUnit.SECONDS.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(gotRequest.isEmpty());
        assertEquals(newRequest.getId(), gotRequest.get(0).getId());

        //Delete the request
        isGood = Boolean.FALSE;
        ElasticSearchController.DeleteRequestsTask deleteRequestsTask = new ElasticSearchController.DeleteRequestsTask();
        try{
            isGood = deleteRequestsTask.execute(newRequest).get();
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
        assertTrue(newRequest.getId() == null);
        assertTrue(isGood);

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
