package com.cmput301fa16t5.legendary_telegram;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Randy on 2016-11-06.
 *
 * Basic idea for asynctask testing from
 * http://stackoverflow.com/questions/2321829/android-asynctask-testing-with-android-test-framework
 * user Billy Brackeen
 */
public class ElasticSearchControllerTest {

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

        //Add the reqeuest
        ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
        addRequestsTask.execute(newRequest);

        //The server wil grant an ID when added sucessfully
        assertFalse(newRequest.getId().isEmpty());
    }

}
