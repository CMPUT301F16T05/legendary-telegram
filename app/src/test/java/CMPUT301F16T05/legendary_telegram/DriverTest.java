package CMPUT301F16T05.legendary_telegram;

import android.location.Location;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 *
 */

public class DriverTest {

    @Test
    public void TestFilterLocation(){
        String name = "test";
        double payment = 12.34;
        Location start = new Location("Start");
        Location end = new Location("End");
        Driver driver = new Driver();
        Request request = new Request(name, payment, start, end);
        ArrayList<Request> requestList = new ArrayList<Request>();
        driver.AcceptRequest(request);
        requestList = driver.FilterLocation();
        assertTrue(requestList.size() == 0);
    }

    @Test
    public void TestFilterKeyword(){
        String name = "test";
        double payment = 12.34;
        Location start = new Location("Start");
        Location end = new Location("End");
        Driver driver = new Driver();
        Request request = new Request(name, payment, start, end);
        ArrayList<Request> requestList = new ArrayList<Request>();
        driver.AcceptRequest(request);
        requestList = driver.FilterKeyword();
        assertTrue(requestList.size() == 0);
    }

    @Test
    public void TestAcceptJob(){
        String name = "test";
        double payment = 12.34;
        Location start = new Location("Start");
        Location end = new Location("End");
        Driver driver = new Driver();
        Request request = new Request(name, payment, start, end);
        driver.AcceptRequest(request);
        assertTrue(driver.ListActiveJobs().size() > 0);
    }

    @Test
    public void TestListAvailableJobs(){
        String name = "test";
        double payment = 12.34;
        Location start = new Location("Start");
        Location end = new Location("End");
        Driver driver = new Driver();
        Request request = new Request(name, payment, start, end);
        ArrayList<Request> requestList = new ArrayList<Request>();
        driver.AcceptRequest(request);
        driver.ListAvailableJobs();
        assertTrue(requestList.size() == 1);
    }

    @Test
    public void TestListActiveJobs(){
        String name = "test";
        double payment = 12.34;
        Location start = new Location("Start");
        Location end = new Location("End");
        Driver driver = new Driver();
        Request request = new Request(name, payment, start, end);
        ArrayList<Request> requestList = new ArrayList<Request>();
        driver.AcceptRequest(request);
        requestList = driver.ListActiveJobs();
        assertTrue(requestList.size() == 0);
    }

    @Test
    public void TestNotifyDriver(){
        //UI test
    }
}
