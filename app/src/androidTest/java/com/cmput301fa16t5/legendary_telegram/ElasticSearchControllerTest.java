package com.cmput301fa16t5.legendary_telegram;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by Randy on 2016-11-06.
 *
 * Basic idea for synchronising asynctask for testing from
 * http://stackoverflow.com/questions/631598/how-to-use-junit-to-test-asynchronous-processes
 * user Strawberry
 */
public class ElasticSearchControllerTest {

    private CountDownLatch lock = new CountDownLatch(1);

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
        //When initialized a request will have an empty ID
        assertTrue(newRequest.getId().isEmpty());

        //Initialize the ESC task and override onPostExecute so I can synchronize the test
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask()
        {
            @Override
            public void onPostExecute(Void result)
            {
                lock.countDown();
            }
        };
        addRequestsTask.execute(newRequest);

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertFalse(newRequest.getId().isEmpty());

        cleanUpRequests();
    }

    @Test
    public void testGetRequestFromID(){
        //Specific details are not important for this test
        Request newRequest = new Request(null, null, null);
        ArrayList<Request> gotRequest = new ArrayList();
        //When initialized a request will have an empty ID
        assertTrue(newRequest.getId().isEmpty());

        //Initialize the ESC task and override onPostExecute so I can synchronize the test
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask()
        {
            @Override
            public void onPostExecute(Void result)
            {
                lock.countDown();
            }
        };
        addRequestsTask.execute(newRequest);

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertFalse(newRequest.getId().isEmpty());

        ElasticSearchController.GetRequestsFromIDTask getRequestsTask = new ElasticSearchController.GetRequestsFromIDTask()
        {
            @Override
            public void onPostExecute(ArrayList<Request> result)
            {
                lock.countDown();
            }
        };
        getRequestsTask.execute(newRequest.getId());

        try {
            gotRequest = getRequestsTask.get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
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
        Request gotRequest = new Request(null, null, null);
        gotRequest.setId("test");
        //When initialized a request will have an empty ID
        assertTrue(newRequest.getId().isEmpty());

        //Initialize the ESC task and override onPostExecute so I can synchronize the test
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask()
        {
            @Override
            public void onPostExecute(Void result)
            {
                lock.countDown();
            }
        };
        addRequestsTask.execute(newRequest);

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //The server wil grant an ID when added sucessfully
        assertFalse(newRequest.getId().isEmpty());

        ElasticSearchController.DeleteRequestsTask deleteRequestsTask = new ElasticSearchController.DeleteRequestsTask()
        {
            @Override
            public void onPostExecute(Void result)
            {
                lock.countDown();
            }
        };
        deleteRequestsTask.execute(newRequest.getId());

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ElasticSearchController.GetRequestsFromIDTask getRequestsTask = new ElasticSearchController.GetRequestsFromIDTask()
        {
            @Override
            public void onPostExecute(ArrayList<Request> result)
            {
                lock.countDown();
            }
        };
        getRequestsTask.execute(newRequest.getId());

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            gotRequest = getRequestsTask.get().get(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(gotRequest.getId().isEmpty());
    }
}
