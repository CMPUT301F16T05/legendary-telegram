package com.cmput301fa16t5.legendary_telegram;

import android.util.Log;

import org.junit.Test;

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
     * Remove any test requests sent to the server
     */
    public void cleanUpRequests(Request... requests){
        for (Request r: requests){
            //do the remove thing
        }
    }

    @Test
    public void testAddSingleRequest(){
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
        Log.d("tag", newRequest.getId());
        assertFalse(newRequest.getId().isEmpty());
    }

}
